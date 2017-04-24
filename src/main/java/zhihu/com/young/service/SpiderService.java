package zhihu.com.young.service;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import utils.DownloadUtil;
import utils.HttpUtil;
import utils.RegUtil;
import zhihu.com.young.elasticsearch.AnswerSearchRepository;
import zhihu.com.young.mapper.AnswerMapper;
import zhihu.com.young.model.Answer;
import zhihu.model.ZhihuAnswer;
import zhihu.model.ZhihuResult;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by young on 2017-4-24.
 */
@Service
public class SpiderService {
    private static final String ACCESS_TOKEN = "Mi4wQUFBQVJlVXRBQUFBRUVJYkpFT2tDeGNBQUFCaEFsVk55WmdoV1FDVnNrUzQ0NWtzMVFPQ3NteDBkS3ZKVlNCTE13|1492822777|ef89f48257a3b1d5697653cff8cab75dcaf94b45";

    private static final String QUESTION_URL_TEMPLATE = "https://www.zhihu.com/api/v4/questions/%s/answers";

    private static final String QUESTION_URL_TEMPLATE_SUFFIX = "?include=data%5B*%5D.is_normal%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Cmark_infos%2Ccreated_time%2Cupdated_time%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&offset&limit=20&sort_by=default";

    private static final Integer PAGE_SIZE = 20;

    private Map<String, Object> queryParams;

    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private AnswerSearchRepository articleSearchRepository;

    @PostConstruct
    public void init() {
        queryParams = Maps.newHashMap();
        queryParams.put("sort_by", "default");
        queryParams.put("include", "data[*].is_normal,is_sticky,collapsed_by,suggest_edit,comment_count,can_comment,content,editable_content,voteup_count,reshipment_settings,comment_permission,mark_infos,created_time,updated_time,relationship.is_authorized,is_author,voting,is_thanked,is_nothelp,upvoted_followees;data[*].author.badge[?(type=best_answerer)].topics");
        queryParams.put("limit", 20);
        queryParams.put("offset", 0);
    }

    public List<ZhihuAnswer> listAnswers(String questionId) {
        List<ZhihuAnswer> answers = Lists.newArrayList();

        System.out.println("start acquire answers........");
        String url = String.format(QUESTION_URL_TEMPLATE, questionId);
        ZhihuResult firstPageResult = HttpUtil.instance().get(url,
                HttpUtil.buildAuthorizationHeaderMap(ACCESS_TOKEN),
                queryParams,
                new com.fasterxml.jackson.core.type.TypeReference<ZhihuResult>() {
                }
        );
        Integer totals = firstPageResult.getPaging().getTotals();
        if (firstPageResult.getData() != null) {
            answers.addAll(firstPageResult.getData());
        }

        Integer offset = PAGE_SIZE;
        for(int i = 0; i< totals/PAGE_SIZE; i++) {
            queryParams.put("offset", offset);
            ZhihuResult currentPage = HttpUtil.instance().get(url,
                    HttpUtil.buildAuthorizationHeaderMap(ACCESS_TOKEN),
                    queryParams,
                    new com.fasterxml.jackson.core.type.TypeReference<ZhihuResult>() {
                    }
            );
            if (currentPage.getData() != null) {
                answers.addAll(currentPage.getData());
            }
            offset += PAGE_SIZE;
        }
        System.out.println("The size of answer is " + answers.size());
        return answers;
    }

    public void downloadImages(String questionId) {
        System.out.println("start........");
        List<ZhihuAnswer> answers = listAnswers(questionId);
        if (CollectionUtils.isEmpty(answers)) {
            return;
        }

        String answerName = answers.get(0).getQuestion().getTitle();
        for (ZhihuAnswer answer : answers) {
            //String folder = answerName + "/" + answer.getId().toString();
            String folder = answerName;
            if (answer.getVoteup_count() != null && answer.getVoteup_count() > 1) {
                Set<String> imageUrls = RegUtil.extractImageSrc(answer.getContent());
                for (String imageUrl : imageUrls) {
                    DownloadUtil.getInstance().downloadImageAndSave(imageUrl, folder);
                }
            }
        }
    }


    public Boolean saveAnswers(String questionId) {
        try {
            List<ZhihuAnswer> answers = listAnswers(questionId);
            for (ZhihuAnswer zhihuAnswer : answers) {
                Answer answer = convertZhihuAnswer(zhihuAnswer);
                Answer existAnswer = answerMapper.selectByPrimaryKey(answer.getId());
                if (existAnswer == null) {
                    answerMapper.insertSelective(answer);
                    articleSearchRepository.save(answer);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Answer convertZhihuAnswer(ZhihuAnswer zhihuAnswer) {
        if (zhihuAnswer == null) {
            return null;
        }

        Answer answer = new Answer();
        if (zhihuAnswer.getId() != null) {
            answer.setId(zhihuAnswer.getId());
        }
        if (StringUtils.isNotEmpty(zhihuAnswer.getEditable_content())) {
            answer.setEditableContent(zhihuAnswer.getEditable_content());
        }
        if (zhihuAnswer.getExcerpt() != null) {
            answer.setExcerpt(zhihuAnswer.getExcerpt());
        }
        if (zhihuAnswer.getCollapsed_by() != null) {
            answer.setCollapsedBy(zhihuAnswer.getCollapsed_by());
        }
        if (zhihuAnswer.getCreated_time() != null) {
            answer.setCreatedTime(zhihuAnswer.getCreated_time());
        }
        if (zhihuAnswer.getUpdated_time() != null) {
            answer.setUpdatedTime(zhihuAnswer.getUpdated_time());
        }
        if (zhihuAnswer.getVoteup_count() != null) {
            answer.setVoteupCount(zhihuAnswer.getVoteup_count());
        }
        if (zhihuAnswer.getIs_collapsed() != null) {
            answer.setIsCollapsed(zhihuAnswer.getIs_collapsed());
        }
        if (zhihuAnswer.getUrl() != null) {
            answer.setUrl(zhihuAnswer.getUrl());
        }
        if (zhihuAnswer.getComment_permission() != null) {
            answer.setCommentPermission(zhihuAnswer.getComment_permission());
        }
        if (StringUtils.isNotEmpty(zhihuAnswer.getContent())) {
            answer.setContent(zhihuAnswer.getContent());
        }
        if (zhihuAnswer.getComment_count() != null) {
            answer.setCommentCount(zhihuAnswer.getComment_count());
        }
        return answer;
    }

}

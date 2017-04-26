package com.young.zhihu.service;

import com.google.common.collect.Maps;
import com.young.elasticsearch.AnswerSearchRepository;
import com.young.mapper.AnswerMapper;
import com.young.message.DownloadImageSender;
import com.young.model.Answer;
import com.young.model.ZhihuAnswer;
import com.young.model.ZhihuResult;
import com.young.utils.DownloadUtil;
import com.young.utils.HttpUtil;
import com.young.utils.RegUtil;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by young on 2017-4-24.
 */
@Service
public class AnswerService {
    private static final String ACCESS_TOKEN = "Mi4wQUFBQVJlVXRBQUFBRUVJYkpFT2tDeGNBQUFCaEFsVk55WmdoV1FDVnNrUzQ0NWtzMVFPQ3NteDBkS3ZKVlNCTE13|1492822777|ef89f48257a3b1d5697653cff8cab75dcaf94b45";

    private static final String QUESTION_URL_TEMPLATE = "https://www.zhihu.com/api/v4/questions/%s/answers";

    private static final String QUESTION_URL_TEMPLATE_SUFFIX = "?include=data%5B*%5D.is_normal%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Cmark_infos%2Ccreated_time%2Cupdated_time%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&offset&limit=20&sort_by=default";

    private static final Integer PAGE_SIZE = 20;

    private Map<String, Object> queryParams;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerSearchRepository articleSearchRepository;

    @Autowired
    private DownloadImageSender downloadImageSender;

    private ExecutorService executorService;


    @PostConstruct
    public void init() {
        queryParams = Maps.newHashMap();
        queryParams.put("sort_by", "default");
        queryParams.put("include", "data[*].is_normal,is_sticky,collapsed_by,suggest_edit,comment_count,can_comment,content,editable_content,voteup_count,reshipment_settings,comment_permission,created_time,updated_time;data[*].author.badge[?(type=best_answerer)].topics");
        queryParams.put("limit", 20);
        queryParams.put("offset", 0);

        executorService = Executors.newFixedThreadPool(30);
    }

    /**
     * 查询指定问题下的所有回答,并可配置的是否下载通过
     * @param questionId        问题id
     * @param isDownloadImage   是否下载图片
     * @return
     */
    public List<ZhihuAnswer> listAnswers(String questionId, Boolean isDownloadImage) {
        List<ZhihuAnswer> answers = Lists.newArrayList();

        System.out.println("First acquire answers........");
        String url = String.format(QUESTION_URL_TEMPLATE, questionId);

        // 抓取answer的第一次请求
        ZhihuResult firstPageResult = HttpUtil.instance().get(url,
                HttpUtil.buildAuthorizationHeaderMap(ACCESS_TOKEN),
                queryParams,
                new com.fasterxml.jackson.core.type.TypeReference<ZhihuResult>() {
                }
        );
        if (firstPageResult == null) {
            System.out.println("Get first answer page error...");
            return answers;
        }

        Integer totals = firstPageResult.getPaging().getTotals();
        if (firstPageResult.getData() != null) {
            answers.addAll(firstPageResult.getData());

            if (isDownloadImage) {
                // 将一次http请求获取到的answer推送到rabbitmq，接收端异步下载answer中包含的图片
                downloadImageSender.send(firstPageResult.getData());
                System.out.println("First acquire answers result: the size of answers is" + firstPageResult.getData().size());
            }
        }

        // 抓取answer的第2,3,4....n次请求
        Integer offset = PAGE_SIZE;
        for(int i = 0; i< totals/PAGE_SIZE; i++) {
            int number = i +2;
            queryParams.put("offset", offset);
            System.out.println(String.format("%d acquire answers........", number));
            ZhihuResult currentPageResult = HttpUtil.instance().get(url,
                    HttpUtil.buildAuthorizationHeaderMap(ACCESS_TOKEN),
                    queryParams,
                    new com.fasterxml.jackson.core.type.TypeReference<ZhihuResult>() {
                    }
            );
            if (currentPageResult == null) {
                continue;
            }

            if (currentPageResult.getData() != null) {
                answers.addAll(currentPageResult.getData());

                if (isDownloadImage) {
                    // answer集合推送到mq中
                    downloadImageSender.send(currentPageResult.getData());
                    System.out.println(number + " acquire answers result: the size of answers is" + currentPageResult.getData().size());
                }
            }
            offset += PAGE_SIZE;
        }
        return answers;
    }

    /**
     * 下载图片
     * @param answers  回答的集合
     */
    public void downloadImages(List<ZhihuAnswer> answers) {
        if (CollectionUtils.isEmpty(answers)) {
            return;
        }

        System.out.println("----------start download images task, the size of answers is " + answers.size());
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

    /**
     * 保存answer到数据库和索引中
     * @param questionId
     * @return
     */
    public Boolean saveAndIndexAnswers(String questionId) {
        try {
            List<ZhihuAnswer> answers = listAnswers(questionId, false);
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

    /**
     * ZhihuAnswer转为Answer
     * @param zhihuAnswer 待转换的bean
     * @return
     */
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

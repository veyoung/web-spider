package zhihu;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.javafx.binding.StringFormatter;
import jdk.internal.org.objectweb.asm.TypeReference;
import org.assertj.core.util.Lists;
import org.springframework.util.CollectionUtils;
import utils.DownloadUtil;
import utils.HttpUtil;
import zhihu.model.ZhihuAnswer;
import zhihu.model.ZhihuPaging;
import zhihu.model.ZhihuResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by young on 2017-4-21.
 */
public class ZhihuSpider {
    private static final String ACCESS_TOKEN = "Mi4wQUFBQVJlVXRBQUFBRUVJYkpFT2tDeGNBQUFCaEFsVk55WmdoV1FDVnNrUzQ0NWtzMVFPQ3NteDBkS3ZKVlNCTE13|1492822777|ef89f48257a3b1d5697653cff8cab75dcaf94b45";

    private static final String QUESTION_URL_TEMPLATE = "https://www.zhihu.com/api/v4/questions/%s/answers";

    private static final String QUESTION_URL_TEMPLATE_SUFFIX = "?include=data%5B*%5D.is_normal%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Cmark_infos%2Ccreated_time%2Cupdated_time%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&offset&limit=20&sort_by=default";

    private static final Integer PAGE_SIZE = 20;

    private Map<String, Object> queryParams;

    public ZhihuSpider() {
        queryParams = Maps.newHashMap();
        queryParams.put("sort_by", "default");
        queryParams.put("include", "data[*].is_normal,is_sticky,collapsed_by,suggest_edit,comment_count,can_comment,content,editable_content,voteup_count,reshipment_settings,comment_permission,mark_infos,created_time,updated_time,relationship.is_authorized,is_author,voting,is_thanked,is_nothelp,upvoted_followees;data[*].author.badge[?(type=best_answerer)].topics");
        queryParams.put("limit", 20);
        queryParams.put("offset", 0);
    }

    public List<ZhihuAnswer> listAnswers(String questionId) {
        List<ZhihuAnswer> answers = Lists.newArrayList();

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
        System.out.println("The size of answer is" + answers.size());
        return answers;
    }

    public static Set<String> getImageSrc(String htmlCode) {
        Set<String> imageSrcs = Sets.newHashSet();
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);

            // src=https://sms.reyo.cn:443/temp/screenshot/zY9Ur-KcyY6-2fVB1-1FSH4.png
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
            imageSrcs.add(src);
        }
        return imageSrcs;
    }

    public void downloadImages(String questionId) {
        System.out.println("start........");
        List<ZhihuAnswer> answers = listAnswers(questionId);
        if (CollectionUtils.isEmpty(answers)) {
            return;
        }

        String answerName = answers.get(0).getQuestion().getTitle();
        for (ZhihuAnswer answer : answers) {
            String folder = answerName + "/" + answer.getId().toString();
            if (answer.getVoteup_count() != null && answer.getVoteup_count() > 1) {
                Set<String> imageUrls = ZhihuSpider.getImageSrc(answer.getContent());
                for (String imageUrl : imageUrls) {
                    DownloadUtil.getInstance().downloadImageAndSave(imageUrl, folder);
                }
            }
        }
    }


    public static void main(String[] args) {
        ZhihuSpider spider = new ZhihuSpider();
        spider.downloadImages("28116784");
    }
}

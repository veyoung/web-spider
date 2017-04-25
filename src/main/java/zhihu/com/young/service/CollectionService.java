package zhihu.com.young.service;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import utils.DownloadUtil;
import utils.JsoupUtil;

import java.util.Set;

/**
 * Created by young on 2017-4-25.
 */
@Service
public class CollectionService {
    private static final String ZHIHU_COLLECTION_URL_PREFIX = "https://www.zhihu.com/collection/%s?page=%d";

    /**
     * 获取某收藏下的所有问题的id
     * @param collectionId
     * @return
     */
    public Set<String> extractQuestionIds(String collectionId) {
        String url = String.format(ZHIHU_COLLECTION_URL_PREFIX, collectionId, 1);
        System.out.println(String.format("Fetching %s...", url));

        int pageSize = 0;
        Set<String> links = JsoupUtil.getInstance().extractRelativeLinksFromPageUrl(url);
        for (String link : links) {
            if (link.contains("?page")) {
                int size = Integer.valueOf(link.split("=")[1]);
                if (size > pageSize) {
                    pageSize = size;
                }
            }
        }

        Set<String> urls = Sets.newHashSet();
        for (int page=1; page <= pageSize; page++) {
            String nextUrl = String.format(ZHIHU_COLLECTION_URL_PREFIX, collectionId, page);
            Set<String> nextLinks = JsoupUtil.getInstance().extractAbsLinksFromPageUrl(nextUrl);
            urls.addAll(nextLinks);
        }

        Set<String> questionIds = Sets.newHashSet();
        for(String preUrl : urls) {
            if (preUrl.contains("question")) {
                String questionId = preUrl.split("/")[4];
                questionIds.add( questionId);
            }
        }
        return questionIds;
    }

    public static void main(String[] args) {
        CollectionService app = new CollectionService();
        Set<String> questionIds = app.extractQuestionIds("46627456");
    }
}

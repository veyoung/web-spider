package pictures;

import utils.DownloadUtil;
import utils.JsoupUtil;

import java.util.Set;

/**
 * Created by young on 2017/4/22.
 */
public class CrawlPictures {

    public static void main(String[] args) {
        String url = "https://www.189sihu.com/Html/63/";
        for (int i = 9749; i> 0; i--) {
            String folderName = "Page" + Integer.valueOf(i);
            String newUrl = url + Integer.valueOf(i) + ".html";
            Set<String> images = JsoupUtil.getInstance().extractImageUrlsFromPageUrl(newUrl);
            images.forEach(imageUrl -> DownloadUtil.getInstance().downloadImageAndSave(imageUrl, folderName));
        }
    }
}

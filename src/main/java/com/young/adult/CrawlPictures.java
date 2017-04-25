package com.young.adult;

import com.google.common.collect.Sets;
import com.young.utils.DownloadUtil;
import com.young.utils.JsoupUtil;

import java.util.Set;

/**
 * Created by young on 2017/4/22.
 */
public class CrawlPictures {
    private static final String URL = "https://www.189sihu.com/Html/63/";

    private static final String URL_TEMPLATE = "https://www.189sihu.com/Html/63/index-%s.html";

    public Set<String> extractLinks() {
        Set<String> links = JsoupUtil.getInstance().extractRelativeLinksFromPageUrl(URL);
        for (int i = 2; i < 10; i++) {
            String realUrl = String.format(URL_TEMPLATE, i);
            Set<String> nextLinks = JsoupUtil.getInstance().extractRelativeLinksFromPageUrl(realUrl);
            links.addAll(nextLinks);
//            nextLinks.forEach(lik -> System.out.println(lik));
        }

        Set<String> result = Sets.newHashSet();
        links.forEach(link -> {
            if(link.indexOf("index") == -1) {
                result.add(link);
            }
        });
        return links;
    }
    public void downloadImages() {
        Set<String> links = extractLinks();
        links.forEach(link -> {
            Set<String> imageUrls = JsoupUtil.getInstance().extractImageUrlsFromPageUrl(link);
            imageUrls.forEach(imageUrl -> {
                String folder = JsoupUtil.getInstance().extractTitleFromPageUrl(link);
                DownloadUtil.getInstance().downloadImageAndSave(imageUrl, folder);
            });
        });
    }

    public static void main(String[] args) {
//        String url = "https://www.189sihu.com/Html/63/";
//        for (int i = 9749; i> 0; i--) {
//            String folderName = "Page" + Integer.valueOf(i);
//            String newUrl = url + Integer.valueOf(i) + ".html";
//            Set<String> images = JsoupUtil.getInstance().extractImageUrlsFromPageUrl(newUrl);
//            images.forEach(imageUrl -> DownloadUtil.getInstance().downloadImageAndSave(imageUrl, folderName));
//        }

        CrawlPictures app = new CrawlPictures();
        app.downloadImages();
    }
}

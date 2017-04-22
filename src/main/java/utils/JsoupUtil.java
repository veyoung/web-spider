package utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by young on 2017-4-20.
 */
public class JsoupUtil {

    private static JsoupUtil intance = new JsoupUtil();

    public static JsoupUtil getInstance() {
        return intance;
    }


    /**
     *
     * @param url
     * @return
     */
    public Set<String> extractImageUrlsFromPageUrl(String url) {
        return extractImageUrlsFromPageUrl(url, 0, 0);
    }

    /**
     *
     * @param url
     * @param minWidth
     * @param minHeight
     * @return
     */
    public Set<String> extractImageUrlsFromPageUrl(String url, Integer minWidth, Integer minHeight) {

        Document document = getDocument(url);
        return extractImageUrls(document, minWidth, minHeight);
    }

    /**
     *
     * @param html
     * @param minWidth
     * @param minHeight
     * @return
     */
    public Set<String> extractImageUrlsFromHtmlText(String html, Integer minWidth, Integer minHeight) {

        Document document = getDocument(html, "");
        return extractImageUrls(document, minWidth, minHeight);
    }

    /**
     *
     * @param url
     * @return
     */
    public List<String> extractLinksFromPageUrl(String url) {

        List<String> urls = Lists.newArrayList();
        Document document = getDocument(url);

        Elements linkElements = document.select("a[href]");
        for (Element link : linkElements) {
            urls.add(link.attr("abs:href"));
        }
        return urls;
    }

    private Document getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return document;
    }

    private Document getDocument(String html, String baseUri) {
        Document document = Jsoup.parse(html,baseUri);
        return document;
    }

    public Set<String> extractImageUrls(Document document, Integer minWidth, Integer minHeight) {
        Set<String> urls = Sets.newHashSet();
        Elements imgElements = document.select("[src]");
        for (Element src : imgElements) {
            if ("img".equals(src.tagName())) {
                String width = src.attr("width");
                String height = src.attr("height");
                if (StringUtils.isEmpty(width) && StringUtils.isEmpty(height)) {
                    width = src.attr("data-rawwidth");
                    height = src.attr("data-rawheight");
                }
                if (!StringUtils.isEmpty(width)) {
                    Integer widthInt = Integer.parseInt(width);
                    if (widthInt < minWidth) {
                        continue;
                    }
                }
                if (!StringUtils.isEmpty(height)) {
                    Integer heightInt = Integer.parseInt(height);
                    if (heightInt < minHeight) {
                        continue;
                    }
                }

                urls.add(src.attr("abs:src"));
            }
        }
        return urls;
    }

    public static void main(String[] args) throws IOException {
//        String url = "http://www.189sihu.com/Html/63/9750.html";
//        System.out.println(String.format("Fetching %s...", url));
////        List<String> links = JsoupUtil.getInstance().extractLinksFromPageUrl(url);
////        links.forEach(link -> System.out.println(link));
//
//        Set<String> images = JsoupUtil.getInstance().extractImageUrlsFromPageUrl(url);
//        System.out.println("The length of images is " + images.size());
//        images.forEach(imageUrl -> DownloadUtil.getInstance().downloadImageAndSave(imageUrl));
    }
}

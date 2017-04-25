package com.young.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Set;

/**
 * Created by young on 2017-4-21.
 */
public class SeleniumUtil {
    private static final String DRIVER_KEY = "webdriver.chrome.driver";

    private static final String  DRIVER_VALUE = "C:\\Users\\young\\chromedriver\\chromedriver.exe";

    private WebDriver webDriver;

    private static SeleniumUtil instance = new SeleniumUtil();

    private SeleniumUtil() {
        System.getProperties().setProperty(DRIVER_KEY, DRIVER_VALUE);
        webDriver = new ChromeDriver();
    }

    public static SeleniumUtil getInstance() {
        return instance;
    }


    public String downloadPageHtml(String url) {
        try {
            webDriver.get(url);
            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            return webElement.getAttribute("outerHTML");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            webDriver.close();
        }
        return new String();
    }

    public static void main(String[] args) {
        String url = "http://www.zhihu.com/question/57443806";
        String content = SeleniumUtil.getInstance().downloadPageHtml(url);

        Set<String> results = JsoupUtil.getInstance().extractImageUrlsFromHtmlText(content, 500, 500);
        Set<String> results2 = JsoupUtil.getInstance().extractImageUrlsFromPageUrl(url, 500, 500);
        System.out.println("extractImageUrlsFromHtmlText() size:" + results.size());
        System.out.println("extractImageUrlsFromPageUrl() size:" + results2.size());
    }
}

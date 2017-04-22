package utils;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by young on 2017-4-21.
 */
public class RegUtil {

    public static Set<String> extractImageSrc(String htmlCode) {
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

    public static void main(String[] args) throws Exception {
    }
}

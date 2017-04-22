package utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by young on 2017-4-21.
 */
public class DownloadUtil {
    //private static final Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

    private static final String DISK_LOCATION_PREFIX = "E:\\images\\";

    private static DownloadUtil instance = new DownloadUtil();

    public static DownloadUtil getInstance() {
        return instance;
    }

    public void downloadImageAndSave(String urlString) {
        downloadImageAndSave(urlString, null);
    }

    /**
     * download image, save on disk
     * @param urlString
     */
    public void downloadImageAndSave(String urlString, String directory) {
        if (StringUtils.isEmpty(urlString)) {
            //logger.info("Parameter[urlString] is absent");
            return;
        }

        if (urlString.indexOf("http") == -1) {
            return;
        }

        urlString = urlString.replaceFirst("https", "http");
        System.out.println(">>Start downloading image " + urlString);

        String[] urlArr = urlString.split("/");
        String fileName = urlArr[urlArr.length - 1];
        if (!fileName.contains(".")) {
            fileName += ".jpg";
        }

        URLConnection con = null;
        try {
            con = new URL(urlString).openConnection();
            con.setConnectTimeout(500 * 1000);
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            String path = StringUtils.isEmpty(directory) ?
                    DISK_LOCATION_PREFIX :
                    DISK_LOCATION_PREFIX + "/" + directory;
            File file = new File(path);
            if (!file.exists()){
                file.mkdirs();
            }

            byte[] bs = new byte[1024];
            int len;
            is = con.getInputStream();
            os = new FileOutputStream(file.getPath() + "\\" + fileName);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DownloadUtil.getInstance()
                .downloadImageAndSave("http://pic4.zhimg.com/v2-e4a561d10d5c8e91d4904921f3e9b627_b.jpg");
    }

}

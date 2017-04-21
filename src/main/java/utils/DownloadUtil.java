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

    /**
     * download image, save on disk
     * @param urlString
     */
    public void downloadImageAndSave(String urlString) {
        if (StringUtils.isEmpty(urlString)) {
            //logger.info("Parameter[urlString] is absent");
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
            con.setConnectTimeout(5 * 1000);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            File file = new File(DISK_LOCATION_PREFIX);
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
                os.close();
                is.close();
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

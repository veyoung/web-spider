package utils;

import com.google.common.collect.Maps;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by young on 2017-4-21.
 */
public class HttpUtil {
    private static final String HEADER_AUTHORIZATION_NAME = "Authorization";

    private OkHttpClient okHttpClient;

    private static HttpUtil instance = new HttpUtil();

    private HttpUtil() {
        okHttpClient = new OkHttpClient();
    }

    private static HttpUtil getInstance() {
        return instance;
    }

    public String get(String url) {
        return get(url,null,null);
    }

    public String get(String url, Map<String, String> headers, Map<String, Object> queryParams) {
        String newUrl = buildUrlParams(url, queryParams);
        Request request = new Request.Builder().url(newUrl).headers(buildHeaders(headers)).build();
        try {
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private Headers buildHeaders(Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (MapUtils.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                headerBuilder.add(key, headers.get(key));
            }
        }
        return headerBuilder.build();
    }

    private String buildUrlParams(String url, Map<String, Object> params) {
        if (StringUtils.isBlank(url) || MapUtils.isEmpty(params)) {
            return url;
        }

        String scheme = url.substring(0, url.indexOf("://"));
        String hostAndPath = url.replace(scheme + "://", "");
        String host = hostAndPath.substring(0, hostAndPath.indexOf("/"));
        String pathSegment = hostAndPath.replace(host, "");
        if (StringUtils.isNotBlank(pathSegment) && pathSegment.startsWith("/")) {
            pathSegment = pathSegment.substring(1);
        }

        String[] urlSegments = new String[3];
        urlSegments[0] = scheme;
        urlSegments[1] = host;
        urlSegments[2] = pathSegment;

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
        urlBuilder.scheme(urlSegments[0]);
        if (urlSegments[1].indexOf(":") > 0) {
            String[] hostAndPort = urlSegments[1].split(":");
            urlBuilder.host(hostAndPort[0]);
            urlBuilder.port(Integer.parseInt(hostAndPort[1]));
        } else {
            urlBuilder.host(urlSegments[1]);
        }
        urlBuilder.addEncodedPathSegments(urlSegments[2]);
        Set<String> paramNames = params.keySet();
        for (String paramName : paramNames) {
            urlBuilder.addQueryParameter(paramName, String.valueOf(params.get(paramName)));
        }
        return urlBuilder.toString();
    }

    public static Map<String, String> buildAuthorizationHeaderMap(String accessToken) {
        Map<String, String> headers = Maps.newHashMap();
        headers.put(HEADER_AUTHORIZATION_NAME, "Bearer " + accessToken);

        return headers;
    }

    public static void main(String[] args) {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        String content = HttpUtil.getInstance().get("http://www.zhihu.com/question/57443806", headers, null);
        System.out.print(content);
    }
}

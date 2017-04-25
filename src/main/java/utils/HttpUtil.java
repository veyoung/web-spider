package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import zhihu.model.ZhihuResult;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangxy-j on 2017/1/22.
 */
public class HttpUtil {

    private static final String ACCESS_TOKEN = "Mi4wQUFBQVJlVXRBQUFBRUVJYkpFT2tDeGNBQUFCaEFsVk55WmdoV1FDVnNrUzQ0NWtzMVFPQ3NteDBkS3ZKVlNCTE13|1492822777|ef89f48257a3b1d5697653cff8cab75dcaf94b45";

    private static HttpUtil instance = new HttpUtil();

    private static final String HEADER_AUTHORIZATION_NAME = "Authorization";

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private OkHttpClient okHttpClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpUtil() {

        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = (hostname, session) -> true;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY)
                .build();
    }

    public static HttpUtil instance() {
        return instance;
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @return
     */
    public ZhihuResult get(String url, Map<String, String> headers) {
        Request request = new Request.Builder().url(url).headers(buildHeaders(headers)).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return objectMapper.readValue(response.body().string(), new TypeReference<ZhihuResult>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            if (response != null) {
                try {
                    System.out.println(String.format("Send http[GET] request to %s error,response string is.....%s", url, response.body().string()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param queryParams
     * @return
     */
    public ZhihuResult get(String url, Map<String, String> headers, Map<String, Object> queryParams, TypeReference<ZhihuResult>
            typeReference) {
        String newUrl = buildUrlParams(url, queryParams);
        Request request = new Request.Builder().url(newUrl).headers(buildHeaders(headers)).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String responseBodyString = response.body().string();
            return objectMapper.readValue(responseBodyString, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
        String[] urlSegments = splitUrlSegments(url);
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

    private String[] splitUrlSegments(String url) {
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

        return urlSegments;
    }

    public static Map<String, String> buildAuthorizationHeaderMap(String accessToken) {
        Map<String, String> headers = Maps.newHashMap();
        headers.put(HEADER_AUTHORIZATION_NAME, "bearer " + accessToken);

        return headers;
    }

    public static void main(String[] args) {
        String url = "https://www.zhihu.com/api/v4/questions/57443806/answers?include=data%5B*%5D.is_normal%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Cmark_infos%2Ccreated_time%2Cupdated_time%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&offset&limit=20&sort_by=default";
        ZhihuResult result = HttpUtil.instance().get(url, HttpUtil.buildAuthorizationHeaderMap(ACCESS_TOKEN));
        System.out.print(result.getPaging().getTotals());
    }
}

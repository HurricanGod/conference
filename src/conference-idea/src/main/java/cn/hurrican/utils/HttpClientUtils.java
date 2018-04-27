package cn.hurrican.utils;

import cn.hurrican.dtl.ResMessage;
import net.sf.json.JSONArray;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpClientUtils {

//    private static Logger logger = LogManager.getLogger(HttpClientUtils.class);

    private static HttpClientContext context = HttpClientContext.create();
    private static RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(120000)
                        .setSocketTimeout(60000)
                        .setConnectionRequestTimeout(60000)
                        .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                        .setExpectContinueEnabled(true)
                        .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                        .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

    //https
    private static SSLConnectionSocketFactory socketFactory;
    private static TrustManager manager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    private static void enableSSL() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{manager}, null);
            socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//            logger.error("enableSSL()方法发生异常：\n{}", e);
            e.printStackTrace();
        }
    }

    /**
     * https get
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    private static CloseableHttpResponse doHttpsGet(String url, String data) {
        enableSSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", socketFactory).build();
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet, context);
        } catch (Exception e) {
//            logger.error("doHttpsGet()方法出现异常：\n{}", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * https post
     *
     * @param url
     * @param values
     * @return
     * @throws IOException
     */
    private static CloseableHttpResponse doHttpsPost(String url, List<NameValuePair> values) {
        enableSSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", socketFactory).build();
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     *  发送 Https Get 请求
     * @param baseUrl 不带请求参数的url
     * @param params 请求参数键值对
     * @return ResMessage{"log":"用于调试的日志，一般空字符串","model":"Map类型","message":"服务端返回的数据","retCode":状态码}
     */
    public static ResMessage sendHttpsGetRequest(String baseUrl, Map<String, String> params){
        StringBuilder url = new StringBuilder(1024);
        url.append(baseUrl);
        if(params != null && params.size() > 0){
            url.append("?");
            params.forEach((k,v) -> url.append(k).append("=").append(v).append("&"));
            int length = url.length();
            url.delete(length -1, length);
        }
        System.out.println("访问的url = \n" + url);
        CloseableHttpResponse httpResponse = doHttpsGet(url.toString(), null);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            return ResMessage.creator().retCodeEqual(statusCode);
        }
        return ResMessage.creator().retCodeEqual(statusCode).msg(toString(httpResponse));
    }

    /**
     * 发送 Https Post 请求
     * @param url 访问的url
     * @param params 提交的请求参数
     * @return ResMessage{"message":"服务端返回的数据","retCode":状态码,"log":"用于调试的日志，一般空字符串","model":"Map类型"}
     */
    public ResMessage sendHttpsPostRequest(String url, Map<String,String> params){
        List<NameValuePair> pairs = new ArrayList<>();

        params.forEach((k,v) -> pairs.add(new BasicNameValuePair(k, v)));

        CloseableHttpResponse response = doHttpsPost(url, pairs);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            return ResMessage.creator().retCodeEqual(statusCode);
        }
        return ResMessage.creator().msg(toString(response)).retCodeEqual(statusCode);
    }

    /**
     * http get
     *
     * @param url
     * @param data
     * @return
     */
    public static CloseableHttpResponse doGet(String url, String data) {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClientBuilder.create().
                setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).
                setRedirectStrategy(new DefaultRedirectStrategy()).
                setDefaultCookieStore(cookieStore).
                setDefaultRequestConfig(requestConfig).build();

        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * http post
     *
     * @param url
     * @param values
     * @return
     */
    public static CloseableHttpResponse doPost(String url, List<NameValuePair> values) {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClientBuilder.create().
                setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).
                setRedirectStrategy(new DefaultRedirectStrategy()).
                setDefaultCookieStore(cookieStore).
                setDefaultRequestConfig(requestConfig).build();

        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 直接把Response内的Entity内容转换成String
     *
     * @param httpResponse
     * @return
     */
    public static String toString(CloseableHttpResponse httpResponse) {
        // 获取响应消息实体
        String result = null;
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Test
    public void testHttpsPostMethod(){
        String url = "https://conference.infoaas.com/conference/conference/conference/latest.do";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("days", "7"));
        params.add(new BasicNameValuePair("page","1"));
        CloseableHttpResponse response = doHttpsPost(url, params);
        String s = toString(response);
        JSONArray jsonArray = JSONArray.fromObject(s);
        jsonArray.forEach(System.out::println);
    }

    @Test
    public void testHttpsGetMethod(){
        String url = "https://conference.infoaas.com/conference/conference/push/queryPopTag.do";

        HashMap<String, String> map = new HashMap<>();
        map.put("startTime", "2018-4-10");
        map.put("offset", "30");
        map.put("top", "8");

        ResMessage response = sendHttpsGetRequest(url, map);
        System.out.println(response);

    }

    public static void main(String[] args) {
        String url = "http://118.89.59.66/conference/conference/push/queryPopTag.do";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("startTime", "2018-4-10"));
        params.add(new BasicNameValuePair("offset","30"));
        params.add(new BasicNameValuePair("top","8"));
        CloseableHttpResponse response = doPost(url, params);

        String s = toString(response);

        System.out.println("s = \n" + s);
    }

}

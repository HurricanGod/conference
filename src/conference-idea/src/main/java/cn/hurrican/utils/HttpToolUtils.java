package cn.hurrican.utils;

import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpToolUtils {
	/**
	 * 连接超时时间
	 */
	public static final int CONNECTION_TIMEOUT_MS = 360000;

	/**
	 * 读取数据超时时间
	 */
	public static final int SO_TIMEOUT_MS = 360000;

	public static final String CONTENT_TYPE_JSON_CHARSET = "application/json;charset=utf-8";

	public static final String CONTENT_TYPE_XML_CHARSET = "application/xml;charset=utf-8";

	/**
	 * httpclient读取内容时使用的字符集
	 */
	public static final String CONTENT_CHARSET = "UTF-8";

	public static final Charset UTF_8 = Charset.forName(CONTENT_CHARSET);

	public static final Charset GBK = Charset.forName("GBK");

	/**
	 * 简单get调用
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String simpleGetInvoke(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException {
		return simpleGetInvoke(url, params, CONTENT_CHARSET);
	}

	/**
	 * 简单get调用
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String simpleGetInvoke(String url, Map<String, String> params, String charset)
			throws ClientProtocolException, IOException, URISyntaxException {

		HttpClient client = buildHttpClient(false);

		HttpGet get = buildHttpGet(url, params);

		HttpResponse response = client.execute(get);

		assertStatus(response);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String returnStr = EntityUtils.toString(entity, charset);
			return returnStr;
		}
		return null;
	}

	public static Header[] simpleHead(String url, Map<String, String> params) throws ClientProtocolException, IOException, URISyntaxException {
		return simpleHeadInvoke(url, params, CONTENT_CHARSET);
	}

	public static Header[] simpleHeadInvoke(String url, Map<String, String> params, String charset) throws ClientProtocolException, IOException, URISyntaxException {
		HttpClient client = buildHttpClient(false);
		HttpHead head = buildhttpHead(url, params);
		HttpResponse response = client.execute(head);
		assertStatus(response);

		if (response.getHeaders("Content-Type") != null) {
			return response.getHeaders("Content-Type");
		}
		return null;
	}

	/**
	 * 简单post调用
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String simplePostInvoke(String url, Map<String, String> params)
			throws URISyntaxException, ClientProtocolException, IOException {
		return simplePostInvoke(url, params, CONTENT_CHARSET);
	}

	/**
	 * 简单post调用
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String simplePostInvoke(String url, Map<String, String> params, String charset)
			throws URISyntaxException, ClientProtocolException, IOException {

		HttpClient client = buildHttpClient(false);

		HttpPost postMethod = buildHttpPost(url, params);

		HttpResponse response = client.execute(postMethod);

		assertStatus(response);

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			String returnStr = EntityUtils.toString(entity, charset);
			return returnStr;
		}

		return null;
	}

	/**
	 * 创建HttpClient
	 *
	 * @param isMultiThread
	 * @return
	 */
	public static HttpClient buildHttpClient(boolean isMultiThread) {

		CloseableHttpClient client;

		if (isMultiThread)
			client = HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
		else
			client = HttpClientBuilder.create().build();
		// 设置代理服务器地址和端口
		// client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
		return client;
	}

	/**
	 * 构建httpPost对象
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildHttpPost(String url, Map<String, String> params)
			throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post);
		HttpEntity he = null;
		if (params != null) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}
			he = new UrlEncodedFormEntity(formparams, UTF_8);
			post.setEntity(he);
		}
		// 在RequestContent.process中会自动写入消息体的长度，自己不用写入，写入反而检测报错
		// setContentLength(post, he);
		return post;

	}

	/**
	 * 构建httpGet对象
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	public static HttpGet buildHttpGet(String url, Map<String, String> params) throws URISyntaxException {
		Assert.notNull(url, "构建HttpGet时,url不能为null");
		HttpGet get = new HttpGet(buildGetUrl(url, params));
		return get;
	}

	public static HttpHead buildhttpHead(String url, Map<String, String> params) throws URISyntaxException {
		Assert.notNull(url, "构建HttpHead时，url不能为Null");
		HttpHead head = new HttpHead(buildGetUrl(url, params));
		return head;
	}

	/**
	 * build getUrl str
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	private static String buildGetUrl(String url, Map<String, String> params) {
		StringBuffer uriStr = new StringBuffer(url);
		if (params != null) {
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				ps.add(new BasicNameValuePair(key, params.get(key)));
			}
			uriStr.append("?");
			uriStr.append(URLEncodedUtils.format(ps, UTF_8));
		}
		return uriStr.toString();
	}

	/**
	 * 设置HttpMethod通用配置
	 *
	 * @param httpMethod
	 */
	public static void setCommonHttpMethod(HttpRequestBase httpMethod) {
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, CONTENT_CHARSET);// setting
																		// contextCoding
		// httpMethod.setHeader(HTTP.CHARSET_PARAM, CONTENT_CHARSET);
		// httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_CHARSET);
		// httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_XML_CHARSET);
	}

	/**
	 * 设置成消息体的长度 setting MessageBody length
	 *
	 * @param httpMethod
	 * @param he
	 */
	public static void setContentLength(HttpRequestBase httpMethod, HttpEntity he) {
		if (he == null) {
			return;
		}
		httpMethod.setHeader(HTTP.CONTENT_LEN, String.valueOf(he.getContentLength()));
	}

	/**
	 * 构建公用RequestConfig
	 *
	 * @return
	 */
	public static RequestConfig buildRequestConfig() {
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT_MS)
				.setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
		return requestConfig;
	}

	/**
	 * 强验证必须是200状态否则报异常
	 *
	 * @param res
	 * @throws HttpException
	 */
	static void assertStatus(HttpResponse res) throws IOException {
		Assert.notNull(res, "http响应对象为null");
		Assert.notNull(res.getStatusLine(), "http响应对象的状态为null");
		switch (res.getStatusLine().getStatusCode()) {
		case HttpStatus.SC_OK:
			// case HttpStatus.SC_CREATED:
			// case HttpStatus.SC_ACCEPTED:
			// case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:
			// case HttpStatus.SC_NO_CONTENT:
			// case HttpStatus.SC_RESET_CONTENT:
			// case HttpStatus.SC_PARTIAL_CONTENT:
			// case HttpStatus.SC_MULTI_STATUS:
			break;
		default:
			throw new IOException("服务器响应状态异常,失败.");
		}
	}

	/**
	 * 简单get调用
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static HttpEntity simpleGetInvokeWithEntity(String url, Map<String, String> params, String charset)
			throws ClientProtocolException, IOException, URISyntaxException {

		HttpClient client = buildHttpClient(false);

		HttpGet get = buildHttpGet(url, params);

		HttpResponse response = client.execute(get);

		assertStatus(response);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			return entity;
		}
		return null;
	}

	public static byte[] simplePostInvokeWithByte(URI uri, JSONObject jsonObject, String charset)
			throws URISyntaxException, ClientProtocolException, IOException {
		InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        byte[] data = null;
        try {
            URL url = new URL(uri.toString());//创建的URL
            if (url!=null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();//打开链接
                httpURLConnection.setConnectTimeout(5000);//设置网络链接超时时间，3秒，链接失败后重新链接
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                // 设置文件类型:
                httpURLConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("accept","application/json");
            	byte[] bypes = jsonObject.toString().getBytes();
            	httpURLConnection.getOutputStream().write(bypes);
                int responseCode = httpURLConnection.getResponseCode();//获取返回码
                if (responseCode == 200) {//成功为200
                    //从服务器获得一个输入流
                   /* inputStream = httpURLConnection.getInputStream();
                    data = new byte[inputStream.available()];
                    inputStream.read(data);
                    httpURLConnection.disconnect();
                    inputStream.close();*/

                    BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int len;
                    byte[] arr = new byte[1024];
                    while((len=bis.read(arr))!= -1){
                        bos.write(arr,0,len);
                        bos.flush();
                    }
                    bos.close();

                    data = bos.toByteArray();
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
       return data;
	}

	private HttpToolUtils() {
	}

	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
//		System.out.println(simpleGetInvoke("http://www.baidu.com", new HashMap<String, String>()));

//
//		map.put("uid", "1");
//		map.put("busiPlatform", "1");
//		map.put("start", "0");
//		map.put("limit", "20");
//		map.put("busiType", "1");
//		map.put("busiId", "1");
//		map.put("traceType", "4");
//		map.put("threshold", "1000");
//		map.put("startTime", "2016-04-28 00:00:00");
//		map.put("endTime", "2016-04-30 00:00:00");
//		map.put("state", "0");
//		map.put("mode", "1");
//		String json = simplePostInvoke("http://gametest.weijuju.com/trace/activity/v1/findUserAlarmRecord.do", map);
//		JSONObject jsonObject = null;
//		try {
//			jsonObject = (JSONObject) JSON.parse(json);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		JSONArray jsonArray = jsonObject.getObject("model").getArray("alarms");
//		System.out.println(jsonObject.getObject("model").get("traceItemId")+":" + jsonObject.get("ret")+":" +jsonObject.get("msg"));
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "asdasdsd");
		params.put("secret", "asdasdsds");
		params.put("grant_type", "authorization_code");
		params.put("js_code", "code");
		String json = simplePostInvoke("https://api.weixin.qq.com/sns/jscode2session", params);
		System.out.println(json);
	}
}

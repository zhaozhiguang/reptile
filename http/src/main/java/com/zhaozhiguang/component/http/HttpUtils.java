package com.zhaozhiguang.component.http;


import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http工具类
 * @author zhiguang
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static int SocketTimeout = 30000;//30秒

    private static int ConnectTimeout = 30000;//30秒

    private static int ConnectionRequestTimeout = 30000;//30秒

    private static Boolean SetTimeOut = true;

    private static PoolingHttpClientConnectionManager connMgr;

    private static RequestConfig requestConfig;

    private static final String DEFAULT_CHARSET = "utf-8";

    static {
        SSLConnectionSocketFactory sslSF = null;
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(trustStore, (x509Certificates, s) -> { return true; }).build();
            sslSF = new SSLConnectionSocketFactory(sslContext);
        } catch (KeyStoreException e) {
            throw new RuntimeException("秘钥文件不对");
        } catch (KeyManagementException e) {
            throw new RuntimeException("秘钥异常");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("ssl套接字异常");
        }
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create().
                register("http", PlainConnectionSocketFactory.INSTANCE).
                register("https", sslSF).build();

        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager(reg);
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        connMgr.setValidateAfterInactivity(1000);


        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(ConnectTimeout);
        // 设置读取超时
        configBuilder.setSocketTimeout(SocketTimeout);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(ConnectionRequestTimeout);

        requestConfig = configBuilder.build();
    }

    /**
     * 获取http请求
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        //构建客户端
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setConnectionManager(connMgr);
        if(SetTimeOut) httpClientBuilder.setDefaultRequestConfig(requestConfig);
        return httpClientBuilder.build();
    }

    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> queries) throws IOException {
        String responseBody = null;
        CloseableHttpClient httpClient = getHttpClient();
        StringBuilder sb = new StringBuilder(url);
        if(queries!=null) appendUrlParam(queries, sb);
        HttpGet httpGet = new HttpGet(sb.toString());
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            logger.info("Executing request " + httpGet.getRequestLine());
            responseBody = getResponseBody(response);
        } catch (Exception ex) {
            logger.error("get请求时发生错误~");
        } finally {
            response.close();
        }
        return responseBody;
    }

    /** post
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @param params  post form 提交的参数
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> queries, Map<String, String> params) throws IOException {
        String responseBody = null;
        CloseableHttpClient httpClient = getHttpClient();
        StringBuilder sb = new StringBuilder(url);
        if(queries!=null) appendUrlParam(queries, sb);
        //指定url,和http方式
        HttpPost httpPost = new HttpPost(sb.toString());
        //添加参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null && params.keySet().size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        //请求数据
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            logger.info("Executing request " + httpPost.getRequestLine());
            responseBody = getResponseBody(response);
        } catch (Exception e) {
            logger.error("post请求时发生错误~");
        } finally {
            response.close();
        }
        return responseBody;
    }

    /**
     * 表单提交
     * @param url
     * @param queries
     * @param params
     * @param files
     * @return
     * @throws Exception
     */
    public static String postForm(String url, Map<String, String> queries, Map<String, String> params, Map<String, File> files) throws IOException {
        String responseBody = null;
        CloseableHttpClient httpClient = getHttpClient();
        StringBuilder sb = new StringBuilder(url);
        if(queries!=null) appendUrlParam(queries, sb);
        //指定url,和http方式
        HttpPost httpPost = new HttpPost(sb.toString());
        MultipartEntityBuilder form = MultipartEntityBuilder.create();
        if (params != null && params.keySet().size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                StringBody body = new StringBody((String) entry.getValue(), ContentType.create("text/plain", Consts.UTF_8));
                form.addPart((String) entry.getKey(), body);
            }
        }
        if(files != null && files.keySet().size() > 0){
            Iterator<Map.Entry<String, File>> iterator = files.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, File> entry = iterator.next();
                FileBody body9 = new FileBody(entry.getValue());
                form.addPart(entry.getKey(), body9);
            }
        }
        HttpEntity entity = form.build();
        httpPost.setEntity(entity);
        //请求数据
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            logger.info("Executing request " + httpPost.getRequestLine());
            responseBody = getResponseBody(response);
        } catch (Exception e) {
            logger.error("post文件表单时发生错误~");
        } finally {
            response.close();
        }
        return responseBody;
    }

    /**
     * Json提交
     * @param url 提交的请求链接
     * @param queries 链接后面的参数
     * @param data json数据
     * @return
     * @throws Exception
     */
    public static String postJson(String url, Map<String, String> queries, String data) throws IOException {
        String responseBody = null;
        CloseableHttpClient httpClient = getHttpClient();
        StringBuilder sb = new StringBuilder(url);
        appendUrlParam(queries, sb);
        HttpPost httpPost = new HttpPost(sb.toString());
        httpPost.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            logger.info("Executing request " + httpPost.getRequestLine());
            responseBody = getResponseBody(response);
        } catch (Exception e) {
            logger.error("postJson表单时发生错误~");
        } finally {
            response.close();
        }
        return responseBody;
    }

    private static String getResponseBody(CloseableHttpResponse response) throws IOException {
        String responseBody = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity responseEntity = response.getEntity();
            responseBody = EntityUtils.toString(responseEntity, DEFAULT_CHARSET);
            logger.debug(responseBody);
            EntityUtils.consume(responseEntity);
            return responseBody;
        } else {
            logger.info("http return status error:" + response.getStatusLine().getStatusCode());
        }
        return responseBody;
    }

    private static void appendUrlParam(Map<String, String> queries, StringBuilder sb){
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator<Map.Entry<String, String>> iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (firstFlag) {
                    sb.append("?" + (String) entry.getKey() + "=" + (String) entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + (String) entry.getKey() + "=" + (String) entry.getValue());
                }
            }
        }
    }
}
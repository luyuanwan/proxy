package com.xiang.proxy;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

import io.netty.handler.codec.http.cookie.DefaultCookie;
import lombok.Data;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.util.Map;

import static org.apache.http.Consts.*;

public class HttpUtils {

    //private static Logger logger = LoggerFactory.getLogger(com.daimler.util.base.HttpUtils.class);
    private static final int SOCK_TIMEOUT = 3;
    private static final int CONN_TIMEOUT = 3;
    private static RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
    private static final int HTTP_STATUS_CODE_SUCCESS = 200;
    private static final String CHARSET_UTF8 = "UTF-8";

    public HttpUtils() {
    }

    public static HttpResponse get(String url,String cookie,boolean isImage) {
        HttpGet method = new HttpGet(url);
        method.addHeader(new BasicHeader("USER-AGENT","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36"));
        method.addHeader("referer",Context.getHomePage());


        if(cookie != null){
            method.addHeader(new BasicHeader("Cookie", "COOKIE_AUTH_TICKET="+cookie));
        }
        return executeMethod(method,cookie,isImage);
    }

    public static HttpResponse get(String url, Map<String, ?> paramsMap,String cookie) {
        url = assembleUrl(url, paramsMap);
        HttpGet method = new HttpGet(url);
        if(cookie != null){
            method.setHeader("COOKIE_AUTH_TICKET",cookie);
        }
        return executeMethod(method,cookie);
    }

    private static String assembleUrl(String url, Map<String, ?> paramsMap) {
        if (paramsMap != null) {
            Set<String> keySet = paramsMap.keySet();
            StringBuilder sb = new StringBuilder(keySet.size() * 8);
            sb.append("?");
            Iterator var4 = keySet.iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();

                try {
                    String value = URLEncoder.encode(String.valueOf(paramsMap.get(key)), "UTF-8");
                    sb.append(key).append("=").append(value).append("&");
                } catch (UnsupportedEncodingException var7) {
                    //logger.error(var7.getMessage(), var7);
                }
            }

            String params = sb.toString();
            url = url + params.substring(0, params.length() - 1);
        }

        return url;
    }

    public static HttpResponse post(String url, Map<String, ?> paramsMap) {
        org.apache.http.client.methods.HttpPost method = new org.apache.http.client.methods.HttpPost(url);
        if (paramsMap != null) {
            List<NameValuePair> nvps = new ArrayList(paramsMap.size());
            Set<String> keySet = paramsMap.keySet();
            Iterator var5 = keySet.iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                nvps.add(new BasicNameValuePair(key, paramsMap.get(key).toString()));
            }

            try {
                method.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException var7) {
                //logger.error(var7.getMessage(), var7);
            }
        }

        return executeMethod(method,null);
    }

    public static String post(String url, Map<String, String> headerMap, Map<String, ?> paramsMap) throws RuntimeException {
        org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url);
        if (headerMap != null && headerMap.size() > 0) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            Iterator var5 = entries.iterator();

            while(var5.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var5.next();
                httpPost.addHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

        if (paramsMap != null && paramsMap.size() > 0) {
            StringEntity params = new StringEntity(JSON.toJSONString(paramsMap), "UTF-8");
            httpPost.setEntity(params);
        }

        return executeMethodV2(httpPost);
    }

    private static final String executeMethodV2(HttpRequestBase httpRequestBase) throws RuntimeException {
        String result = "";
        CloseableHttpClient client = HttpClients.createDefault();
        RequestConfig configSelf = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
        httpRequestBase.setConfig(configSelf);

        String var5;
        try {
            org.apache.http.HttpResponse response = client.execute(httpRequestBase);
            if (200 != response.getStatusLine().getStatusCode()) {
                return result;
            }

            var5 = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception var15) {
            //throw new HttpException(var15);
            throw new RuntimeException("");

        } finally {
            httpRequestBase.releaseConnection();

            try {
                client.close();
            } catch (IOException var14) {
                //throw new HttpException(var14);
                throw new RuntimeException("");
            }
        }

        return var5;
    }

    public static HttpResponse post(String url, String body, String requestHeader) {
        org.apache.http.client.methods.HttpPost method = new org.apache.http.client.methods.HttpPost(url);
        method.setEntity(new StringEntity(body, ContentType.create(requestHeader, "UTF-8")));
        return executeMethod(method,null);
    }

    public static HttpResponse postXml(String url, String body) {
        org.apache.http.client.methods.HttpPost method = new org.apache.http.client.methods.HttpPost(url);
        method.setEntity(new StringEntity(body, ContentType.create("text/xml", "UTF-8")));
        return executeMethod(method,null);
    }

    public static HttpResponse postJson(String url, List<? extends NameValuePair> params) {
        org.apache.http.client.methods.HttpPost method = new org.apache.http.client.methods.HttpPost(url);
        method.setEntity(new UrlEncodedFormEntity(params, UTF_8));

        method.addHeader("Content-Type","application/json");
        return executeMethod(method,null);
    }

    public static HttpResponse postJsonBody(String url, String params,String cookie) {
        org.apache.http.client.methods.HttpPost method = new org.apache.http.client.methods.HttpPost(url);
        method.setEntity(new StringEntity(params,"utf-8"));

        method.addHeader("Content-Type","application/json");
        if(cookie != null) {
            method.addHeader(new BasicHeader("Cookie", "COOKIE_AUTH_TICKET="+cookie));
        }
        return executeMethod(method,cookie);
    }


    public static CloseableHttpClient getHttps(BasicCookieStore cookieStore){
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setDefaultCookieStore(cookieStore).setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return  HttpClients.createDefault();
    }

    private static final HttpResponse executeMethod(HttpRequestBase method,String cookie) {
        return executeMethod(method, cookie,false);
    }

    private static final HttpResponse executeMethod(HttpRequestBase method,String cookie,boolean isImage) {

        HttpResponse httpResponse = new HttpUtils.HttpResponse();
        httpResponse.setSuccess(false);
        String result = null;
        byte[] resultBytes = null;


        method.setConfig(config);

        BasicCookieStore cookieStore = new BasicCookieStore();
        if(cookie != null){
            BasicClientCookie c = new BasicClientCookie("COOKIE_AUTH_TICKET",cookie);
            c.setPath("/");
            c.setExpiryDate(DateUtils.getDateAfter(new Date(),1));
            c.setVersion(1);
           cookieStore.addCookie(c);
        }
        HttpContext localContext = new BasicHttpContext();

        method.addHeader("Cookie", cookieStore.toString());


        CloseableHttpClient client = getHttps(cookieStore);
        try {

            org.apache.http.HttpResponse response = client.execute(method,localContext);
            if (200 == response.getStatusLine().getStatusCode()) {
                httpResponse.setSuccess(true);
            }

            if (isImage) {
                resultBytes = EntityUtils.toByteArray(response.getEntity());
            }
           else {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }

            httpResponse.setResult(result);
           httpResponse.setResultBytes(resultBytes);
        } catch (Exception var13) {
            //logger.error(var13.getMessage(), var13);
            httpResponse.setResult(var13.getMessage());
        } finally {
            method.releaseConnection();

            try {
                client.close();
            } catch (IOException var12) {
                //logger.error(var12.getMessage(), var12);
            }

        }

        return httpResponse;
    }


    @Data
    public static class HttpResponse {
        private boolean success;
        private String result;

        private byte[] resultBytes;

        public void setResultBytes(byte[] b){
            this.resultBytes = b;
        }

        public byte[] getResultBytes(){
            return resultBytes;
        }

        public HttpResponse() {
        }

        public boolean isSuccess() {
            return this.success;
        }

        void setSuccess(boolean success) {
            this.success = success;
        }

        public String getResult() {
            return this.result;
        }

        void setResult(String result) {
            this.result = result;
        }
    }
}

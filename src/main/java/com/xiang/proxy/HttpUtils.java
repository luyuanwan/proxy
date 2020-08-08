package com.xiang.proxy;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.Map;

import static org.apache.http.Consts.*;

public class HttpUtils {

    //private static Logger logger = LoggerFactory.getLogger(com.daimler.util.base.HttpUtils.class);
    private static final int SOCK_TIMEOUT = 3;
    private static final int CONN_TIMEOUT = 3;
    private static RequestConfig config = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
    private static final int HTTP_STATUS_CODE_SUCCESS = 200;
    private static final String CHARSET_UTF8 = "UTF-8";

    public HttpUtils() {
    }

    public static HttpResponse get(String url) {
        HttpGet method = new HttpGet(url);
        return executeMethod(method);
    }

    public static HttpResponse get(String url, Map<String, ?> paramsMap) {
        url = assembleUrl(url, paramsMap);
        HttpGet method = new HttpGet(url);
        return executeMethod(method);
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

        return executeMethod(method);
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
        return executeMethod(method);
    }

    public static HttpResponse postXml(String url, String body) {
        org.apache.http.client.methods.HttpPost method = new org.apache.http.client.methods.HttpPost(url);
        method.setEntity(new StringEntity(body, ContentType.create("text/xml", "UTF-8")));
        return executeMethod(method);
    }

    public static HttpResponse postJson(String url, List<? extends NameValuePair> params) {
        org.apache.http.client.methods.HttpPost method = new org.apache.http.client.methods.HttpPost(url);
        method.setEntity(new UrlEncodedFormEntity(params, UTF_8));
        return executeMethod(method);
    }

    private static final HttpResponse executeMethod(HttpRequestBase method) {
        HttpResponse httpResponse = new HttpUtils.HttpResponse();
        httpResponse.setSuccess(false);
        String result = null;
        CloseableHttpClient client = HttpClients.createDefault();
        method.setConfig(config);

        try {
            org.apache.http.HttpResponse response = client.execute(method);
            if (200 == response.getStatusLine().getStatusCode()) {
                httpResponse.setSuccess(true);
            }

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            httpResponse.setResult(result);
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


    public static class HttpResponse {
        private boolean success;
        private String result;

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

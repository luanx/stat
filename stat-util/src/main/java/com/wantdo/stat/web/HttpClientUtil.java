package com.wantdo.stat.web;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HttpClient工具类
 *
 * @ Date : 15/10/7
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class HttpClientUtil {

    private static final int TIMEOUT_SECONDS = 20;
    private static final int POOL_SIZE = 20;

    private static CloseableHttpClient httpClient;

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public HttpClientUtil() {
    }

    public static synchronized CloseableHttpClient getDefaultHttpClient(){
        if (httpClient == null){
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SECONDS * 1000)
                    .setConnectTimeout(TIMEOUT_SECONDS * 1000).build();
            httpClient = HttpClientBuilder.create().setMaxConnTotal(POOL_SIZE)
                    .setDefaultRequestConfig(requestConfig).build();
        }
        return httpClient;
    }

    public static void destroyHttpClient(){
        try {
            httpClient.close();
        } catch (IOException e){
            logger.error("httpclient close fail", e);
        }
    }

    /**
     * Get方式提交请求
     */
    public static String get(String url){
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = getDefaultHttpClient();
        CloseableHttpResponse remoteResponse = null;
        try {
            remoteResponse = httpClient.execute(httpGet);
            //判断返回值
            int statusCode = remoteResponse.getStatusLine().getStatusCode();
            if (statusCode >= 400){
                logger.error("fetch data error from " + url);
                return null;
            }

            HttpEntity entity = remoteResponse.getEntity();

            //输出内容
            if (entity == null){
                return null;
            }
            return EntityUtils.toString(entity);

        } catch (IOException e){
            logger.error("httpclient error", e);
            return null;
        } finally {
            try{
                if (remoteResponse != null){
                    remoteResponse.close();
                }
            } catch (Exception e){
                logger.error("httpclient close fail", e);
            }
        }

    }

}

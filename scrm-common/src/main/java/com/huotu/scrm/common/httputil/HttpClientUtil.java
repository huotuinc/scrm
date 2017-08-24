/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.scrm.common.httputil;

import com.huotu.scrm.common.utils.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liual on 2015-11-11.
 */
public class HttpClientUtil {
    private static final Log log = LogFactory.getLog(HttpClientUtil.class);
    private static HttpClientUtil httpClientUtil = new HttpClientUtil();

    private HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        return httpClientUtil;
    }

    private CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create()
//                .setDefaultRequestConfig(RequestConfig.custom()
//                        .setConnectionRequestTimeout(30000)
//                        .setConnectTimeout(30000)
//                        .setSocketTimeout(30000).build())
                .build();
    }

    public HttpResult post(String url, Map<String, Object> requestMap) {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            requestMap.forEach((key, value) -> {
                if (value != null) {
                    nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(value)));
                }
            });
            HttpPost httpPost = new HttpPost(url);
            HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs, Constant.UTF8);
            httpPost.setEntity(httpEntity);
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String httpContent = EntityUtils.toString(response.getEntity());
                log.debug("post response:" + httpContent);
                return new HttpResult(response.getStatusLine().getStatusCode(),httpContent );
            }
        } catch (IOException e) {
            log.error("post error",e);
            return new HttpResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param requestMap
     */
    public void postAsync(String url, Map<String, Object> requestMap) {
        // TODO: 7/7/16
    }

    public HttpResult get(String url, Map requestMap) {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            StringBuilder finalUrl = new StringBuilder(url);
            Iterator iterator = requestMap.entrySet().iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (entry.getValue() != null) {
                    if (index == 0) {
                        finalUrl.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    } else {
                        finalUrl.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), Constant.UTF8));
                    }
                }
                index++;
            }
            HttpGet httpGet = new HttpGet(finalUrl.toString());
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                log.info("response status code:" + response.getStatusLine().getStatusCode());
                String responseContent = EntityUtils.toString(response.getEntity());
                log.info("response content:" + responseContent);
                return new HttpResult(response.getStatusLine().getStatusCode(), responseContent);
            }
        } catch (IOException e) {
            log.error("get error",e);
            return new HttpResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

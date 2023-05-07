package com.simplefaas.executor.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.*;


import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



public class HttpUtil {
    public static String sendPostWithJson(String url, String jsonStr, HashMap<String,String> headers) {
        // 返回的结果
        String jsonResult = "";
        try {
            HttpClient client = new HttpClient();
            // 连接超时
            client.getHttpConnectionManager().getParams().setConnectionTimeout(3*1000);
            // 读取数据超时
            client.getHttpConnectionManager().getParams().setSoTimeout(3*60*1000);
            client.getParams().setContentCharset("UTF-8");
            PostMethod postMethod = new PostMethod(url);

            postMethod.setRequestHeader("content-type", headers.get("content-type"));

            // 非空
            if (null != jsonStr && !"".equals(jsonStr)) {
                StringRequestEntity requestEntity = new StringRequestEntity(jsonStr, headers.get("content-type"), "UTF-8");
                postMethod.setRequestEntity(requestEntity);
            }
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                jsonResult = postMethod.getResponseBodyAsString();
            } else {
                throw new RuntimeException("接口连接失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("接口连接失败！");
        }
        return jsonResult;
    }

    public static String requestForHttp(String url, Map<String, String> requestParams) throws Exception {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(5000).build();
        /** HttpPost */
        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(requestConfig);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> it = requestParams.entrySet().iterator();
//		System.out.println(params.toString());
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            if (value != null) {
                params.add(new BasicNameValuePair(key, value));
            }
        }

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        /** HttpResponse */

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                System.out.println("## release resouce error ##" + e);

            }
        }
        return result;
    }


}


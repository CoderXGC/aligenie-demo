package com.ylesb.demo.controller;
/**
 * @title: PushNotificationsShowHttpDemo
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/914:08
 */


import com.ylesb.demo.utils.Client;
import com.ylesb.demo.utils.TeaRequest;
import com.ylesb.demo.utils.TimeUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className    : PushNotificationsShowHttpDemo
 * @description  : [不使用sdk，直接发送http请求，存在bug]
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/9 14:08]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/9 14:08]
 * @updateRemark : [描述说明本次修改内容] 
 */

public class PushNotificationsShowHttpDemo {

    private static String ACCESS_KEY = "你的accessKey";
    private static String ACCESS_SECRET = "你的accessSecret";
    private static String HTTP_URL = "http://openapi.aligenie.com/v1.0/iap/notifications";

    public static void main(String[] args) {

        System.out.println(getISO8601Timestamp(new Date()) + " UTC");
        Map<String, String> query = new HashMap<>();

        Map<String, String> headers = new HashMap<>();
        headers.put("x-acs-date", TimeUtil.getUtcTime());
        headers.put("x-acs-action", "PushNotifications");
        headers.put("x-acs-version", "iap_1.0");
        headers.put("Content-Type", "application/json");
        headers.put("x-acs-signature-nonce", UUID.randomUUID().toString());
        TeaRequest req = new TeaRequest();
        req.query = query;
        req.headers = headers;
        req.method = "PUT";
        req.pathname = "/v1.0/iap/notifications";
        try {

            //模板占位符填充信息
            Map<String, Object> placeHolder = new HashMap<>();
            placeHolder.put("ip", "192.168.0.1");

            Map<String, Object> sendTarget = new HashMap<>();
            sendTarget.put("TargetIdentity", "教程中获取的id");
            //你使用什么就获取写什么
            sendTarget.put("TargetType", "USER_OPEN_ID");

            Map<String, Object> notificationUnicastRequest = new HashMap<>();
            notificationUnicastRequest.put("PlaceHolder", placeHolder);
            notificationUnicastRequest.put("MessageTemplateId", "7hYJkLiqM381wUQr");
            notificationUnicastRequest.put("EncodeType", "SKILL_ID");
            notificationUnicastRequest.put("SendTarget", sendTarget);
            notificationUnicastRequest.put("EncodeKey", "88074");
            notificationUnicastRequest.put("OrganizationId","1325195918262396007");
            // 是否是调试
            notificationUnicastRequest.put("IsDebug",false);

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("NotificationUnicastRequest", notificationUnicastRequest);
            bodyMap.put("TenantInfo", new HashMap<>());

            Gson gson = new Gson();
            String body = gson.toJson(bodyMap);
            System.out.println(body);
            String authorization = Client.getAuthorization(req,
                    body, ACCESS_KEY, ACCESS_SECRET);
            headers.put("Authorization", authorization);

            HttpPut httpPut = new HttpPut(HTTP_URL);
            if (StringUtils.isNotEmpty(body)) {
                httpPut.setEntity(new StringEntity(body, Consts.UTF_8));
            }
            if (!headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPut.addHeader(entry.getKey(), entry.getValue());
                }
            }

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(2000)
                    .setSocketTimeout(2000).build();
            CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .setMaxConnTotal(200)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            HttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity, Consts.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     * @param date
     * @return
     */
    public static String getISO8601Timestamp(Date date){
        TimeZone tz = TimeZone.getTimeZone("GMT");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return nowAsISO;
    }
    // 根据Date时间生成UTC时间函数
    public static String generateUTCTime(Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        dateFormat.setLenient(false);
        return dateFormat.format(time);
    }
}

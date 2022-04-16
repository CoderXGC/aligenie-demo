package com.ylesb.demo.controller;
/**
 * @title: Sample
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/159:54
 */

import com.aliyun.tea.*;
import com.aliyun.aligenieiap_1_0.models.*;
import com.aliyun.teaopenapi.models.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @className    : Sample
 * @description  : [描述说明该类的功能]  
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/15 9:54]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/15 9:54]
 * @updateRemark : [描述说明本次修改内容] 
 */


public class Sample {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.aligenieiap_1_0.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "openapi.aligenie.com";
        return new com.aliyun.aligenieiap_1_0.Client(config);
    }

    public static void main(String[] args_) throws Exception {

        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.aligenieiap_1_0.Client client = Sample.createClient("技能应用你的accesskey", "技能应用你的accesskeySecret");
        PushNotificationsRequest pushNotificationsRequest = new PushNotificationsRequest();
        //模板占位符填充信息
        Map<String, String> placeHolder = new HashMap<>();
        placeHolder.put("ip", "192.168.0.1");



        PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequest notificationUnicastRequest1=new PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequest();
        PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequestSendTarget sendTarget1 = new PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequestSendTarget();
        sendTarget1.setTargetType("USER_OPEN_ID");
        sendTarget1.setTargetIdentity("id\u003d");
        notificationUnicastRequest1.setSendTarget(sendTarget1);
        notificationUnicastRequest1.setMessageTemplateId("技能消息模板id");
        notificationUnicastRequest1.setEncodeType("SKILL_ID");
        notificationUnicastRequest1.setEncodeKey("技能id");
        notificationUnicastRequest1.setPlaceHolder(placeHolder);
        notificationUnicastRequest1.setIsDebug(true);
      //  notificationUnicastRequest1.setOrganizationId("1325195918262396007");
        pushNotificationsRequest.setNotificationUnicastRequest(notificationUnicastRequest1);
        pushNotificationsRequest.setTenantInfo(new PushNotificationsRequest.PushNotificationsRequestTenantInfo());

        client.pushNotifications(pushNotificationsRequest);
        try {
            client.pushNotifications(pushNotificationsRequest);
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                System.out.println("code: " + err.code + ", message: " + err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                System.out.println("code: " + err.code + ", message: " + err.message);
            }

        }
    }
}
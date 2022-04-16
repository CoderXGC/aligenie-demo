package com.ylesb.demo.controller;
/**
 * @title: WebSocket
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/1520:11
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.aliyun.aligenieiap_1_0.models.PushNotificationsRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.web.bind.annotation.RestController;
/**
 * @className    : WebSocket
 * @description  : [描述说明该类的功能]  
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/15 20:11]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/15 20:11]
 * @updateRemark : [描述说明本次修改内容] 
 */

@ServerEndpoint("/WebSocket/{id}")
@RestController
public class WebSocket {
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

    // 存储会话
    private static ConcurrentHashMap<String, WebSocket> webSocket = new ConcurrentHashMap<String, WebSocket>();

    private String id;
    private Session session;

    /**
     * 接入连接回调
     *
     * @param session 会话对象
     * @param id      会话ID
     * @throws Exception 异常
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) throws Exception {
        this.id = id;
        this.session = session;
        webSocket.put(id, this);
        // 检验后端能否正常给前端发送信息
        sendMessageToId(this.id, "客户端你好，我是后端，我正在通过WebSocket监控服务状态！");
        System.out.println(id + "接入连接监控！");
    }

    /**
     * 关闭连接回调
     */
    @OnClose
    public void onClose() throws Exception {
        webSocket.remove(this.id);
        //java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.aligenieiap_1_0.Client client = WebSocket.createClient("技能应用accesskey", "技能应用accesskey");
        PushNotificationsRequest pushNotificationsRequest = new PushNotificationsRequest();
        //模板占位符填充信息
        Map<String, String> placeHolder = new HashMap<>();
        placeHolder.put("ip", "192.168.0.1");



        PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequest notificationUnicastRequest1=new PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequest();
        PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequestSendTarget sendTarget1 = new PushNotificationsRequest.PushNotificationsRequestNotificationUnicastRequestSendTarget();
        sendTarget1.setTargetType("USER_OPEN_ID");
        sendTarget1.setTargetIdentity("你的应用id\u003d");
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
        System.out.println(this.id + "关闭连接，模拟断网负载过高异常");
    }

    /**
     * 收到客户端发来消息回调
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println(this.id + "发来消息：" + message);
    }

    /**
     * 会话出现错误回调
     *
     * @param error   错误信息
     */
    @OnError
    public void onError(Throwable error) {

    }

    /**
     * 发送消息给客户端
     *
     * @param message 消息
     * @throws IOException 异常
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 给指定的会话发送消息
     *
     * @param id      会话ID
     * @param message 消息
     * @throws IOException 异常
     */
    public void sendMessageToId(String id, String message) throws IOException {
        webSocket.get(id).sendMessage(message);
    }

    /**
     * 群发消息
     *
     * @param message 消息
     * @throws IOException 异常
     */
    public void sendMessageToAll(String message) throws IOException {
        for (String key : webSocket.keySet()) {
            try {
                webSocket.get(key).sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



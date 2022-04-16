package com.ylesb.demo.config;
/**
 * @title: WebSocketConfig
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/1520:08
 */

/**
 * @className    : WebSocketConfig
 * @description  : [描述说明该类的功能]  
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/15 20:08]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/15 20:08]
 * @updateRemark : [描述说明本次修改内容] 
 */

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Auther: liaoshiyao
 * @Date: 2019/1/11 11:49
 * @Description: 配置类
 */
@Component
public class WebSocketConfig {

    /**
     * ServerEndpointExporter 作用
     *
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

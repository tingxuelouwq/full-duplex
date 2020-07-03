package com.kevin.fdx.sockjs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * MyWebSocketMessageBrokerConfigurer<br/>
 * com.kevin.fdx.sockjs.config<br/>
 * kevin<br/>
 * 2020/7/1 16:33<br/>
 * 1.0<br/>
 */
@Configuration
@EnableWebSocketMessageBroker   // 此注解表示使用STOMP协议来传输基于消息代理的消息
class MyWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册STOMP的端点
     * addEndpoint: 添加STOMP协议的端点，该URL是供websocket或sockjs客户端访问的地址
     * withSockJS: 指定端点使用sockjs协议
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-simple")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * 配置消息代理
     * 启用简单Broker，消息的发送地址符合配置的前缀的消息才会被发送到该Broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
    }
}

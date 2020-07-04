package com.kevin.fdx.sockjs.config;

import com.kevin.fdx.sockjs.interceptor.MyChannelInterceptor;
import com.kevin.fdx.sockjs.interceptor.MyHandShakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * kevin<br/>
 * 2020/7/3 15:21<br/>
 */
@Configuration
@EnableWebSocketMessageBroker
@DependsOn(value = "myChannelInterceptor")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private MyHandShakeInterceptor myHandShakeInterceptor;

    @Autowired
    private MyChannelInterceptor myChannelInterceptor;

    /**
     * 注册STOMP的端点
     * addEndpoint: 添加STOMP协议的端点，该URL是供websocket或sockjs客户端访问的地址
     * withSockJS: 指定端点使用sockjs协议
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-simple")
                .setAllowedOrigins("*")
                .addInterceptors(myHandShakeInterceptor)    // 添加自定义拦截
                .withSockJS();
        registry.addEndpoint("/websocket-simple-single")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(myChannelInterceptor);
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

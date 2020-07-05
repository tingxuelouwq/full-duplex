package com.kevin.fdx.cluster.config;

import com.kevin.fdx.cluster.handler.AuthHandShakeInterceptor;
import com.kevin.fdx.cluster.handler.AuthWebSocketHandlerDecoratorFactory;
import com.kevin.fdx.cluster.handler.MyPrincipalHandShakeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * kevin<br/>
 * 2020/7/3 15:21<br/>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private MyPrincipalHandShakeHandler myPrincipalHandShakeHandler;

    @Autowired
    private AuthHandShakeInterceptor authHandShakeInterceptor;

    @Autowired
    private AuthWebSocketHandlerDecoratorFactory authWebSocketHandlerDecoratorFactory;

    /**
     * 注册STOMP的端点
     * addEndpoint: 添加STOMP协议的端点，该URL是供websocket或sockjs客户端访问的地址
     * withSockJS: 指定端点使用sockjs协议
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-cluster-rabbitmq")
                .addInterceptors(authHandShakeInterceptor)
                .setHandshakeHandler(myPrincipalHandShakeHandler)
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * 配置消息代理
     * 使用RabbitMQ作为消息代理，替换默认的Simple Broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // STOMP Broker Relay将消息发送到外部消息代理
        registry.enableStompBrokerRelay("/exchange", "/topic", "/queue", "/amq/queue")
                .setRelayHost("localhost")      // rabbitmq-host服务器地址
                .setRelayPort(61613)            // rabbitmq-stomp服务器端口
                .setClientLogin("guest")        // 登录账户
                .setClientPasscode("guest");    // 登录密码
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(authWebSocketHandlerDecoratorFactory);
    }
}

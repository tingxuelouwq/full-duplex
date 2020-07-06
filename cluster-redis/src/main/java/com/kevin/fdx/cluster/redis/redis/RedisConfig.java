package com.kevin.fdx.cluster.redis.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * kevin<br/>
 * 2020/7/6 20:48<br/>
 */
@Configuration
public class RedisConfig {

    /**
     * redis消息监听容器，可以添加多个监听不同通道的redis监听器
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 订阅了一个叫chat的通道
        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
        return container;
    }

    /**
     * 消息监听适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法处理监听到的消息
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        // 默认的消息处理方法是"handleMessage"
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}

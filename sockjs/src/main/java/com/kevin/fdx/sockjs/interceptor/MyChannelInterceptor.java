package com.kevin.fdx.sockjs.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * kevin<br/>
 * 2020/7/3 10:48<br/>
 */
@Component
public class MyChannelInterceptor implements ChannelInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        logger.info("[{}] preSend", getClass().getSimpleName());
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        logger.info("command: {}", command.name());
        // 检测用户订阅内容（防止用户订阅不合法频道）
        if (StompCommand.SUBSCRIBE.equals(command)) {
            logger.info("[{}]用户订阅目的地={}", getClass().getSimpleName(), accessor.getDestination());
            // 如果该用户订阅的频道不合法，则直接返回null，前端用户就接收不到该频道信息
            return message;
        } else {
            return message;
        }
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        logger.info("[{}] afterSendCompletion", getClass().getSimpleName());
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        logger.info("command: {}", command.name());
        if (StompCommand.SUBSCRIBE.equals(command)) {
            logger.info("[{}]订阅消息发送成功", getClass().getSimpleName());
            simpMessagingTemplate.convertAndSend("/topic/geResponse", "消息发送成功");
        }
        // 如果用户断开连接
        if (StompCommand.DISCONNECT.equals(command)) {
            logger.info("[{}]用户断开连接", getClass().getSimpleName());
            simpMessagingTemplate.convertAndSend("/topic/getResponse", "{'msg':'用户断开连接成功'}");
        }
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        logger.info("[{}] preReceive", getClass().getSimpleName());
        return true;
    }
}

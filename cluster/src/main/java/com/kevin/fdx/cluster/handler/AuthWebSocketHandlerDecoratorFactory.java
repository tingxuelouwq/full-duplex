package com.kevin.fdx.cluster.handler;

import com.kevin.fdx.cluster.service.RedisSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.security.Principal;

@Component
public class AuthWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisSessionService redisSessionService;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                // 客户端与服务端建立连接后，记录谁上线了
                Principal principal = session.getPrincipal();
                if (principal != null) {
                    String username = principal.getName();
                    String sessionId = session.getId();
                    logger.info("websocket online: " + username + ", sessionId: " + sessionId);
                    redisSessionService.add(username, sessionId);
                }
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                // 客户端与服务端断开连接后，记录谁下线了
                Principal principal = session.getPrincipal();
                if (principal != null) {
                    String username = principal.getName();
                    String sessionId = session.getId();
                    logger.info("websocket offline: " + username + ", sessionId: " + sessionId);
                    redisSessionService.delete(username);
                }
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }
}

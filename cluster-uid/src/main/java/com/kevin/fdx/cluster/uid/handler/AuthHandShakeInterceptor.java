package com.kevin.fdx.cluster.uid.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class AuthHandShakeInterceptor implements HandshakeInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
        HttpSession session = servletRequest.getServletRequest().getSession(false);
        String loginName = session.getAttribute("loginName") + "";

        logger.info("===>握手前, loginName: " + loginName);

        if (StringUtils.isEmpty(loginName)) {
            logger.error("未登录系统，禁止登录websocket!");
            return false;
        } else {
            attributes.put("loginName", loginName); //将loginName放入WebSocketSession中
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        logger.info("===>握手后");
    }
}

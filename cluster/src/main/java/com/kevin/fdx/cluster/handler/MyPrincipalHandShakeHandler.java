package com.kevin.fdx.cluster.handler;

import com.kevin.fdx.core.util.JsonUtil;
import com.kevin.fdx.cluster.dto.MyPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Component
public class MyPrincipalHandShakeHandler extends DefaultHandshakeHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        HttpSession session = getSession(request);
        String loginName = session.getAttribute("loginName") + "";
        if (StringUtils.isEmpty(loginName)) {
            logger.error("未登录系统，禁止登录websocket!");
            return null;
        }
        Principal principal = new MyPrincipal(loginName);
        logger.info("===>握手, MyPrincipalHandShakeHandler principal: " + JsonUtil.bean2Json(principal));
        return principal;
    }

    // 参考HttpSessionHandshakeInterceptor
    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(false);
        }
        return null;
    }
}

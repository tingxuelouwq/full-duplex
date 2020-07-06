package com.kevin.fdx.cluster.redis.server;

import com.kevin.fdx.cluster.redis.dto.ResponseDTO;
import com.kevin.fdx.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint(value = "/socket/{username}")
public class WebSocketServer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 记录当前在线连接数
     */
    public static AtomicInteger online = new AtomicInteger();
    /**
     * 用来存放每个客户端对应的WebSocketServer对象
     */
    public static Map<String, Session> sessionPools = new ConcurrentHashMap<>();

    /**
     * 发送消息
     * @param session   客户端与socket建立的会话
     * @param message   消息
     * @throws IOException
     */
    public void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 发送消息
     * @param username  建立连接的客户端用户名
     * @param message   消息
     * @throws IOException
     */
    public void sendMessage(String username, String message) throws IOException {
        Session session = sessionPools.get(username);
        sendMessage(session, message);
    }

    /**
     * 连接建立成功时调用
     * @param session   客户端与socket建立的会话
     * @param username  建立连接的客户端用户名
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username)
            throws IOException {
        sessionPools.put(username, session);
        online.incrementAndGet();
        logger.info(username + "加入websocket,当前人数为" + online.get());
        sendMessage(session, "欢迎" + username + "加入连接");
    }

    /**
     * 关闭连接时调用
     * @param username  关闭连接的客户端用户名
     */
    @OnClose
    public void onClose(@PathParam(value = "name") String username) {
        sessionPools.remove(username);
        online.decrementAndGet();
        logger.info(username + "断开websocket连接,当前人数为" + online.get());
    }

    /**
     * 收到客户端消息时调用(群发)
     * @param message   消息
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        logger.info("websocket收到一条消息:" + message);
        ResponseDTO responseDTO = JsonUtil.json2Bean(message, ResponseDTO.class);
        String username = responseDTO.getUsername();
        String payload = responseDTO.getMessage();
        if (sessionPools.get(username) != null) {
            sessionPools.get(username).getBasicRemote().sendText(payload);
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("发生错误, " + throwable.getMessage());
    }
}

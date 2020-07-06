package com.kevin.fdx.cluster.redis.redis;

import com.kevin.fdx.cluster.redis.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * kevin<br/>
 * 2020/7/6 21:09<br/>
 */
@Component
public class MessageReceiver {

    @Autowired
    private WebSocketServer webSocketServer;

    public void receiveMessage(String message) throws IOException {
        webSocketServer.onMessage(message);
    }
}

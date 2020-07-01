package com.kevin.fdx.demo.controller;

import com.kevin.fdx.demo.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SocketController {

    @Autowired
    private WebSocketServer webSocketServer;

    @GetMapping("/socket")
    public void testSocket1(@RequestParam String username,
                            @RequestParam String message) throws IOException {
        webSocketServer.sendMessage(username, message);
    }

    @GetMapping("/socket/all")
    public void testSocket2(@RequestParam String message) throws IOException {
        webSocketServer.onMessage(message);
    }
}

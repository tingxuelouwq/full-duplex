package com.kevin.fdx.rabbitmq.controller;

import com.kevin.fdx.core.util.JsonUtil;
import com.kevin.fdx.rabbitmq.dto.RequestDTO;
import com.kevin.fdx.rabbitmq.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BroadcastController<br/>
 * com.kevin.fdx.sockjs.controller<br/>
 * kevin<br/>
 * 2020/7/1 16:23<br/>
 * 1.0<br/>
 */
@RestController
public class BroadcastController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 收到消息计数
     */
    private AtomicInteger count = new AtomicInteger(0);

    @MessageMapping("/receive-rabbitmq")
//    @SendTo("/exchange/rabbitmq/get-response")
//    @SendTo("/queue/rabbitmq")
//    @SendTo("/amq/queue/rabbitmq2")
    @SendTo("/topic/get-response")
    public ResponseDTO broadcast(RequestDTO requestDTO) {
        logger.info("receive message: " + JsonUtil.bean2Json(requestDTO));
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseMessage(getClass().getSimpleName() + " receive [" +
                count.incrementAndGet() + "] records");
        return responseDTO;
    }

    @GetMapping("/broadcast-rabbitmq/index")
    public ModelAndView broadcastIndex(HttpServletRequest request) {
        logger.info("remote host: " + request.getRemoteHost());

        ModelAndView modelAndView = new ModelAndView("/websocket/rabbitmq/ws-broadcast-rabbitmq");
        return modelAndView;
    }
}

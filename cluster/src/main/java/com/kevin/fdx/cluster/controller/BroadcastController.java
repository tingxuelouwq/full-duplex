package com.kevin.fdx.cluster.controller;

import com.kevin.fdx.core.util.JsonUtil;
import com.kevin.fdx.cluster.dto.ResponseDTO;
import com.kevin.fdx.cluster.service.RedisSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisSessionService redisSessionService;

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("/websocket/cluster/login");
    }

    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request,
                               String username,
                               String password) {
        HttpSession session = request.getSession();
        session.setAttribute("loginName", username);
        return new ModelAndView("/websocket/cluster/ws-cluster-rabbitmq");
    }

    @GetMapping("/send")
    public ModelAndView sendToUserPage(String username, String message) {
        return new ModelAndView("/websocket/sendtouser/send");
    }

    @PostMapping("/sendToUser")
    public void sendToUser(String username, String message) {
        logger.info("===> 接收到消息, username: {}, message: {}", username, message);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseMessage(message);

        // 根据用户名获取对应的websocket sessionid
        String wsSessionId = redisSessionService.get(username);
        // 生成路由键，规则为: websocket订阅目的地+"-user"+websocket的sessionid
        String routingKey = getTopicRoutingKey("demo", wsSessionId);

        logger.info("===> 发送消息, username: {}, message: {}, sessionid: {}, routingKey: {}",
                username, JsonUtil.bean2Json(responseDTO), wsSessionId, routingKey);
        rabbitTemplate.convertAndSend("amq.topic", routingKey, JsonUtil.bean2Json(responseDTO));
    }

    private String getTopicRoutingKey(String destination, String sessionId) {
        return destination + "-user" + sessionId;
    }
}

package com.kevin.fdx.touser.controller;

import com.kevin.fdx.core.util.JsonUtil;
import com.kevin.fdx.touser.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("/websocket/sendtouser/login");
    }

    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request,
                               String username,
                               String password) {
        HttpSession session = request.getSession();
        session.setAttribute("loginName", username);
        return new ModelAndView("/websocket/sendtouser/ws-sendtouser-rabbitmq");
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
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/demo", JsonUtil.bean2Json(responseDTO));
    }
}

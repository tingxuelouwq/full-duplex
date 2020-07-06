package com.kevin.fdx.cluster.redis.controller;

import com.kevin.fdx.cluster.redis.dto.ResponseDTO;
import com.kevin.fdx.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("/login");
    }

    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request,
                               String username) {
        HttpSession session = request.getSession();
        session.setAttribute("loginName", username);
        return new ModelAndView("/client");
    }

    @GetMapping("/send")
    public ModelAndView sendToUserPage(String username, String message) {
        return new ModelAndView("/websocket/sendtouser/send");
    }

    @PostMapping("/sendToUser")
    public void sendToUser(HttpServletRequest request, String username, String message) {
        logger.info("controller收到一条消息, port: {}, username: {}, message: {}",  request.getRemotePort(), username, message);
        ResponseDTO responseDTO = new ResponseDTO(username, message);
        stringRedisTemplate.convertAndSend("chat", JsonUtil.bean2Json(responseDTO));
    }
}

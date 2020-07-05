package com.kevin.fdx.cluster.uid.service.impl;

import com.kevin.fdx.cluster.uid.service.RedisSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * kevin<br/>
 * 2020/7/5 21:51<br/>
 */
@Service
public class RedisSessionServiceImpl implements RedisSessionService {

    @Autowired
    private RedisTemplate<String, String> template;

    /**
     * key=登录用户名
     * value=websocket的sessionid
     */
    private ConcurrentHashMap<String, String> redisHashMap = new ConcurrentHashMap<>(32);

    @Override
    public void add(String name, String wsSessionId) {
        template.opsForValue().set(name, wsSessionId, 24 * 3600, TimeUnit.SECONDS);
    }

    @Override
    public boolean delete(String name) {
        return template.delete(name);
    }

    @Override
    public String get(String name) {
        return template.opsForValue().get(name);
    }
}

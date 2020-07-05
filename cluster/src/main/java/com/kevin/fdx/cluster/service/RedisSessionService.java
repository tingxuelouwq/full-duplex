package com.kevin.fdx.cluster.service;

/**
 * kevin<br/>
 * 2020/7/5 20:05<br/>
 */
public interface RedisSessionService {

    /**
     * 缓存用户和websocket sessionid的信息
     */
    void add(String name, String wsSessionId);

    /**
     * 从缓存中删除用户信息
     */
    boolean delete(String name);

    /**
     * 从缓存中根据用户名获取对应的websocket sessionid
     */
    String get(String name);
}

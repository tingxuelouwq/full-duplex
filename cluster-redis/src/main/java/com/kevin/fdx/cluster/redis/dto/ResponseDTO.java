package com.kevin.fdx.cluster.redis.dto;

/**
 * ResponseDTO<br/>
 * com.kevin.fdx.sockjs.dto<br/>
 * kevin<br/>
 * 2020/7/1 16:18<br/>
 * 1.0<br/>
 */
public class ResponseDTO {

    private String username;
    private String message;

    public ResponseDTO() {
    }

    public ResponseDTO(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

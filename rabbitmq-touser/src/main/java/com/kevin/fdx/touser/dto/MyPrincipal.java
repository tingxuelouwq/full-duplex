package com.kevin.fdx.touser.dto;

import java.security.Principal;

public class MyPrincipal implements Principal {

    private String loginName;

    public MyPrincipal(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getName() {
        return loginName;
    }
}

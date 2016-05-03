package com.joint.web.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 用户登录的token
 */
public class StatelessFormToken implements AuthenticationToken {

    private String username;
    private String password;

    public StatelessFormToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
       return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}

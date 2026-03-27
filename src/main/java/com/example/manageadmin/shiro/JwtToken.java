package com.example.manageadmin.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JWT Token 类
 * 用于 Shiro 认证
 */
public class JwtToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }
}

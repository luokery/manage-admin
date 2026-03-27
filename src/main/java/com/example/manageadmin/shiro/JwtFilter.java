package com.example.manageadmin.shiro;

import com.example.manageadmin.cosnst.ResultEnum;
import com.example.manageadmin.model.pojo.JwtProperties;
import com.example.manageadmin.model.vo.ResponseVO;
import com.example.manageadmin.model.vo.Result;
import com.example.manageadmin.shiro.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * JWT 过滤器
 * 拦截请求并验证 JWT Token
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends AuthenticatingFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final JwtProperties jwtProperties;
    
    /**
     * 创建登录的身份token
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = getTokenFromRequest(httpRequest);
        return new JwtToken(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
//        // 检查是否是排除路径
//        String requestURI = httpRequest.getRequestURI();
//        if (isExcludedPath(requestURI)) {
//            return true; // 允许访问
//        }
        
        String token = getTokenFromRequest(httpRequest);

        if (!StringUtils.hasText(token)) {
            writeUnauthorizedResponse(response, ResultEnum.Unauthorized.message());
            return false;
        }

        if (!jwtUtil.validateToken(token)) {
            writeUnauthorizedResponse(response, "Token 已过期或无效，请重新登录");
            return false;
        }

        return executeLogin(request, response);
    }

    /**
     * 检查路径是否在排除列表中
     */
    private boolean isExcludedPath(String requestURI) {
        for (String pattern : jwtProperties.getExcludePaths()) {
            if (pathMatcher.match(pattern, requestURI)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("JWT 登录失败: {}", e.getMessage());
        writeUnauthorizedResponse(response, "认证失败: " + e.getMessage());
        return false;
    }

    /**
     * 从请求中获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(jwtUtil.getHeader());
        if (StringUtils.hasText(header) && header.startsWith(jwtUtil.getPrefix())) {
            return header.substring(jwtUtil.getPrefix().length());
        }
        return null;
    }

    /**
     * 写入未授权响应
     */
    private void writeUnauthorizedResponse(ServletResponse response, String message) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setCharacterEncoding("UTF-8");

        try {
        	ResponseVO<Void> result = Result.build(HttpStatus.UNAUTHORIZED.value(), message);
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } catch (IOException e) {
            log.error("写入响应失败", e);
        }
    }

    /**
     * 判断是否允许访问（用于 OPTIONS 请求）
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 允许 OPTIONS 请求通过
        if (RequestMethod.OPTIONS.name().equalsIgnoreCase(httpRequest.getMethod())) {
            return true;
        }
        
        // 检查是否是排除路径
        String requestURI = httpRequest.getRequestURI();
        if (isExcludedPath(requestURI)) {
            return true;
        }
        
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
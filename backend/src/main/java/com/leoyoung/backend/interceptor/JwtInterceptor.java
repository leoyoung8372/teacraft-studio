package com.leoyoung.backend.interceptor;

import com.leoyoung.backend.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 登录拦截器 —— 拦截所有需要登录的接口
 * 职责：校验 Authorization 头中的 Bearer Token，有效则放行并在 request 中注入用户信息
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    /** 构造器注入 JwtUtils */
    public JwtInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 放行 CORS 预检请求（OPTIONS）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        // 缺少 Token 或格式不正确（必须以 "Bearer " 开头）
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        // 去掉 "Bearer " 前缀，取出纯 Token
        String token = authHeader.substring(7);
        if (!jwtUtils.isValid(token)) {
            response.setStatus(401);
            return false;
        }

        // Token 有效，将用户信息注入 request，方便 Controller 直接获取
        request.setAttribute("userId", jwtUtils.getUserId(token));
        request.setAttribute("userType", jwtUtils.getUserType(token));
        return true;
    }
}

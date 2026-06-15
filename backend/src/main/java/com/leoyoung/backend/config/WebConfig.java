package com.leoyoung.backend.config;

import com.leoyoung.backend.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 全局配置
 * 负责：跨域配置 + JWT 拦截器注册
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    /**
     * 跨域配置
     * 允许前端（商家端 Vue 和用户端 UniApp）从不同端口/域名访问后端
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")        // 对所有 API 路径生效
                .allowedOriginPatterns("*")   // 允许任意来源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
                .allowedHeaders("*")          // 允许所有请求头
                .allowCredentials(true);      // 允许携带 Cookie（Token 放 Header，此项可保留）
    }

    /**
     * 注册 JWT 拦截器
     * 拦截 /api/** 下所有请求，排除登录和注册接口（公开访问）
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")           // 拦截所有 API
                .excludePathPatterns(
                        "/api/user/login",            // 用户登录
                        "/api/user/register",         // 用户注册
                        "/api/admin/login"            // 管理员登录
                );
    }
}

package com.leoyoung.backend.dto;

/**
 * 管理员登录响应 DTO
 */
public class AdminLoginResponse {

    private String token;
    private Long adminId;
    private String username;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

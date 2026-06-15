package com.leoyoung.backend.dto;

/**
 * 用户登录响应 DTO
 * 返回前端需要的用户信息和 Token
 */
public class LoginResponse {

    private String token;       // JWT Token
    private Long userId;        // 用户 ID
    private String nickname;    // 昵称
    private String avatarUrl;   // 头像 URL

    // ========== Getter / Setter ==========

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

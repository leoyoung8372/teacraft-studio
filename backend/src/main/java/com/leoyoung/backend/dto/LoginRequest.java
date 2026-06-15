package com.leoyoung.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录请求 DTO
 * 接收前端传来的手机号、密码
 */
public class LoginRequest {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

    // ========== Getter / Setter ==========

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

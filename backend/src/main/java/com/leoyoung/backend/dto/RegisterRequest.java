package com.leoyoung.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户注册请求 DTO
 * 接收前端传来的昵称、手机号、密码
 */
public class RegisterRequest {

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

    // ========== Getter / Setter ==========

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

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

package com.leoyoung.backend.common;

/**
 * 统一响应状态码枚举
 * 集中管理所有 code 和 message，避免前端解析时出现魔法数字
 */
public enum ResultCode {

    // ========== HTTP 通用状态码 ==========
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // ========== 业务状态码（1001 起） ==========
    USER_EXISTS(1001, "用户已存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    STOCK_INSUFFICIENT(2001, "库存不足"),
    ORDER_NOT_FOUND(3001, "订单不存在");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

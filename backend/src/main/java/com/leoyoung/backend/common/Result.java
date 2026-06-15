package com.leoyoung.backend.common;

/**
 * 统一响应体 —— 所有 Controller 返回此结构
 * 前端统一按 {code, message, data} 格式解析
 *
 * @param <T> 携带的数据类型
 */
public class Result<T> {

    private int code;
    private String message;
    private T data;

    /** 私有构造，强制通过静态工厂方法创建 */
    private Result() {}

    // ========== 成功响应 ==========

    /** 成功，不返回数据（适用于新增、修改、删除操作） */
    public static <T> Result<T> success() {
        return success(null);
    }

    /** 成功，返回数据（适用于查询操作） */
    public static <T> Result<T> success(T data) {
        return success(data, ResultCode.SUCCESS.getMessage());
    }

    /** 成功，自定义消息 */
    public static <T> Result<T> success(T data, String message) {
        Result<T> r = new Result<>();
        r.code = ResultCode.SUCCESS.getCode();
        r.message = message;
        r.data = data;
        return r;
    }

    // ========== 失败响应 ==========

    /** 失败，使用预定义状态码和默认消息 */
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> r = new Result<>();
        r.code = resultCode.getCode();
        r.message = resultCode.getMessage();
        return r;
    }

    /** 失败，使用预定义状态码 + 自定义消息（允许覆盖默认描述） */
    public static <T> Result<T> error(ResultCode resultCode, String message) {
        Result<T> r = new Result<>();
        r.code = resultCode.getCode();
        r.message = message;
        return r;
    }

    // ========== Getter ==========

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}

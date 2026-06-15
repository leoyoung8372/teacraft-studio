package com.leoyoung.backend.common.exception;

import com.leoyoung.backend.common.ResultCode;

/**
 * 业务异常 —— Service 层业务校验不通过时抛出
 * 由 GlobalExceptionHandler 统一拦截，包装为 Result 返回给前端
 */
public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;

    /** 使用预定义状态码，消息取默认值 */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /** 覆盖默认消息（需要更具体提示时使用） */
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}

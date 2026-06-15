package com.leoyoung.backend.common.handler;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.common.ResultCode;
import com.leoyoung.backend.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 拦截所有 Controller 层抛出的异常，统一包装为 Result 返回
 * 确保前端始终收到 {code, message, data} 结构的响应
 */
@RestControllerAdvice // 等价于 @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常 —— 返回对应的业务状态码和消息
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getResultCode(), e.getMessage());
    }

    /**
     * 参数校验异常（@Valid 校验失败时触发）
     * 提取第一条校验错误信息返回给前端
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "参数校验失败";
        log.warn("参数校验异常: {}", msg);
        return Result.error(ResultCode.BAD_REQUEST, msg);
    }

    /**
     * 兜底处理 —— 未预料的其他异常统一返回 500
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常:", e); // 打印完整堆栈，方便排查
        return Result.error(ResultCode.INTERNAL_ERROR);
    }
}

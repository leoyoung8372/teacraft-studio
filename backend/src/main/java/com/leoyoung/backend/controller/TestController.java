package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.common.ResultCode;
import com.leoyoung.backend.common.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 临时测试 Controller —— 验证 common 层基础设施
 * 确认 Result / BusinessException / GlobalExceptionHandler 正常工作后即可删除
 */
@RestController
public class TestController {

    /**
     * 场景1：正常响应
     * 预期 → {"code":200,"message":"操作成功","data":"hello"}
     */
    @GetMapping("/test/success")
    public Result<String> testSuccess() {
        return Result.success("hello");
    }

    /**
     * 场景2：业务异常
     * 预期 → {"code":1001,"message":"用户已存在","data":null}
     */
    @GetMapping("/test/biz-error")
    public Result<Void> testBizError() {
        throw new BusinessException(ResultCode.USER_EXISTS);
    }

    /**
     * 场景3：系统异常
     * 预期 → {"code":500,"message":"服务器内部错误","data":null}
     */
    @GetMapping("/test/sys-error")
    public Result<Void> testSysError() {
        throw new RuntimeException("模拟未知异常");
    }
}

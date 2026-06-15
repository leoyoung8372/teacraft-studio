package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.dto.OrderCreateRequest;
import com.leoyoung.backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 订单 Controller（用户端）
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** 创建订单 */
    @PostMapping("/create")
    public Result<Void> create(@RequestBody OrderCreateRequest request, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        orderService.create(userId, request);
        return Result.success(null, "下单成功");
    }

    /** 订单列表 */
    @GetMapping("/list")
    public Result<?> list(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return Result.success(orderService.list(userId));
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(orderService.detail(id));
    }
}

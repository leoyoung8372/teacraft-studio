package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理 Controller（商家端）
 */
@RestController
@RequestMapping("/api/admin/orders")
public class OrderAdminController {

    private final OrderService orderService;

    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** 待处理订单 */
    @GetMapping("/pending")
    public Result<?> pending() {
        return Result.success(orderService.listPending());
    }

    /** 完成订单 */
    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        orderService.complete(id);
        return Result.success(null, "订单已完成");
    }

    /** 取消订单 */
    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.success(null, "订单已取消，库存已恢复");
    }

    /** 历史订单 */
    @GetMapping("/history")
    public Result<?> history() {
        return Result.success(orderService.listHistory());
    }
}

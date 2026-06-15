package com.leoyoung.backend.service;

import com.leoyoung.backend.dto.OrderCreateRequest;
import com.leoyoung.backend.dto.OrderResponse;
import com.leoyoung.backend.entity.OrderInfo;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /** 创建订单（扣库存 + 清购物车，事务） */
    void create(Long userId, OrderCreateRequest request);

    /** 用户端：订单列表 */
    List<OrderInfo> list(Long userId);

    /** 用户端/商家端：订单详情（含明细） */
    OrderResponse detail(Long orderId);

    /** 商家端：待处理订单 */
    List<OrderInfo> listPending();

    /** 商家端：完成订单 */
    void complete(Long orderId);

    /** 商家端：取消订单（恢复库存） */
    void cancel(Long orderId);

    /** 商家端：历史订单（已完成+已取消） */
    List<OrderInfo> listHistory();
}

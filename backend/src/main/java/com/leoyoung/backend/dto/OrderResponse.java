package com.leoyoung.backend.dto;

import com.leoyoung.backend.entity.OrderItem;
import com.leoyoung.backend.entity.OrderInfo;

import java.util.List;

/**
 * 订单详情响应 DTO —— 组装订单主表 + 明细列表
 */
public class OrderResponse {

    private OrderInfo order;            // 订单主体信息
    private List<OrderItem> items;      // 订单明细（商品快照）

    public OrderInfo getOrder() { return order; }
    public void setOrder(OrderInfo order) { this.order = order; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}

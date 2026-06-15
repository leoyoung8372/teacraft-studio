package com.leoyoung.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leoyoung.backend.common.ResultCode;
import com.leoyoung.backend.common.exception.BusinessException;
import com.leoyoung.backend.dto.OrderCreateRequest;
import com.leoyoung.backend.dto.OrderResponse;
import com.leoyoung.backend.entity.*;
import com.leoyoung.backend.mapper.*;
import com.leoyoung.backend.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现 —— 最复杂的业务模块，涉及双表写入、库存扣减/恢复、事务控制
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderInfoMapper orderInfoMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderInfoMapper orderInfoMapper, OrderItemMapper orderItemMapper,
                            CartItemMapper cartItemMapper, ProductMapper productMapper) {
        this.orderInfoMapper = orderInfoMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional // 创建订单全过程在同一事务中，任一步失败则全部回滚
    public void create(Long userId, OrderCreateRequest request) {
        // 1. 获取购物车
        LambdaQueryWrapper<CartItem> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(CartItem::getUserId, userId);
        List<CartItem> cartItems = cartItemMapper.selectList(cartWrapper);
        if (cartItems.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "购物车为空");
        }

        // 2. 生成订单号（时间戳 + UUID 前8位）
        String orderNo = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 3. 遍历购物车：校验库存、扣库存、构建订单明细快照
        for (CartItem cart : cartItems) {
            Product product = productMapper.selectById(cart.getProductId());
            if (product == null || product.getStock() < cart.getQuantity()) {
                throw new BusinessException(ResultCode.STOCK_INSUFFICIENT,
                        "「" + (product != null ? product.getName() : "未知商品") + "」库存不足");
            }
            // 扣库存
            product.setStock(product.getStock() - cart.getQuantity());
            productMapper.updateById(product);

            // 构建订单明细（快照）
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductImageUrl(product.getImageUrl());
            item.setPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            item.setSweetness(cart.getSweetness());
            item.setTemperature(cart.getTemperature());
            item.setCupSize(cart.getCupSize());
            item.setAddOns(cart.getAddOns());
            orderItems.add(item);
            total = total.add(item.getSubtotal());
        }

        // 4. 创建订单主记录
        OrderInfo order = new OrderInfo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setMealType(request.getMealType());
        order.setAddressId(request.getAddressId());
        order.setStatus("making");
        order.setPaidAt(LocalDateTime.now()); // 模拟支付时间
        orderInfoMapper.insert(order);

        // 5. 保存订单明细（关联订单 ID）
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        // 6. 清空购物车
        cartItemMapper.delete(cartWrapper);
    }

    @Override
    public List<OrderInfo> list(Long userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId, userId)
               .orderByDesc(OrderInfo::getCreatedAt);
        return orderInfoMapper.selectList(wrapper);
    }

    @Override
    public OrderResponse detail(Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> items = orderItemMapper.selectList(wrapper);

        OrderResponse resp = new OrderResponse();
        resp.setOrder(order);
        resp.setItems(items);
        return resp;
    }

    @Override
    public List<OrderInfo> listPending() {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getStatus, "making")
               .orderByAsc(OrderInfo::getCreatedAt); // 先下单先处理
        return orderInfoMapper.selectList(wrapper);
    }

    @Override
    public void complete(Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order != null) {
            order.setStatus("finished");
            orderInfoMapper.updateById(order);
        }
    }

    @Override
    @Transactional // 取消需恢复库存，同一事务
    public void cancel(Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) return;

        // 恢复库存：遍历订单明细，逐个加回
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                productMapper.updateById(product);
            }
        }

        order.setStatus("cancelled");
        orderInfoMapper.updateById(order);
    }

    @Override
    public List<OrderInfo> listHistory() {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderInfo::getStatus, "finished", "cancelled")
               .orderByDesc(OrderInfo::getCreatedAt);
        return orderInfoMapper.selectList(wrapper);
    }
}

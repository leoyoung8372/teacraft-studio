package com.leoyoung.backend.service;

import com.leoyoung.backend.dto.CartAddRequest;
import com.leoyoung.backend.dto.CartItemResponse;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartItemService {

    /** 加入购物车（同商品同规格则累加数量） */
    void add(Long userId, CartAddRequest request);

    /** 查询用户购物车列表（含商品信息和价格） */
    List<CartItemResponse> list(Long userId);

    /** 修改商品数量 */
    void updateQuantity(Long id, Integer quantity);

    /** 删除购物车条目 */
    void remove(Long id);
}

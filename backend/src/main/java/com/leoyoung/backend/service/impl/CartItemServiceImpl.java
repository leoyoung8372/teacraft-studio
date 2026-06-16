package com.leoyoung.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leoyoung.backend.common.ResultCode;
import com.leoyoung.backend.common.exception.BusinessException;
import com.leoyoung.backend.dto.CartAddRequest;
import com.leoyoung.backend.dto.CartItemResponse;
import com.leoyoung.backend.entity.CartItem;
import com.leoyoung.backend.entity.Product;
import com.leoyoung.backend.mapper.CartItemMapper;
import com.leoyoung.backend.mapper.ProductMapper;
import com.leoyoung.backend.service.CartItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车服务实现
 */
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;

    public CartItemServiceImpl(CartItemMapper cartItemMapper, ProductMapper productMapper) {
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
    }

    @Override
    public void add(Long userId, CartAddRequest request) {
        // 校验库存：加购前检查商品是否存在、是否上架、库存是否充足
        Product product = productMapper.selectById(request.getProductId());
        if (product == null || product.getIsOnSale() != 1) {
            throw new BusinessException(ResultCode.NOT_FOUND, "商品已下架或不存在");
        }

        // 查询用户购物车中是否已有同商品+同规格的记录
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .eq(CartItem::getProductId, request.getProductId())
               .eq(request.getSweetness() != null, CartItem::getSweetness, request.getSweetness())
               .eq(request.getTemperature() != null, CartItem::getTemperature, request.getTemperature())
               .eq(request.getCupSize() != null, CartItem::getCupSize, request.getCupSize())
               .eq(request.getAddOns() != null, CartItem::getAddOns, request.getAddOns());
        CartItem existing = cartItemMapper.selectOne(wrapper);

        if (existing != null) {
            // 已存在 → 累加数量前校验总数量不超过库存
            int newQty = existing.getQuantity() + request.getQuantity();
            if (newQty > product.getStock()) {
                throw new BusinessException(ResultCode.STOCK_INSUFFICIENT,
                        "「" + product.getName() + "」库存不足，当前仅剩 " + product.getStock() + " 件");
            }
            existing.setQuantity(newQty);
            cartItemMapper.updateById(existing);
        } else {
            // 不存在 → 新增前校验数量不超过库存
            if (request.getQuantity() > product.getStock()) {
                throw new BusinessException(ResultCode.STOCK_INSUFFICIENT,
                        "「" + product.getName() + "」库存不足，当前仅剩 " + product.getStock() + " 件");
            }
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(request.getProductId());
            item.setQuantity(request.getQuantity());
            item.setSweetness(request.getSweetness());
            item.setTemperature(request.getTemperature());
            item.setCupSize(request.getCupSize());
            item.setAddOns(request.getAddOns());
            cartItemMapper.insert(item);
        }
    }

    @Override
    public List<CartItemResponse> list(Long userId) {
        // 查用户购物车
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId).orderByDesc(CartItem::getCreatedAt);
        List<CartItem> items = cartItemMapper.selectList(wrapper);

        List<CartItemResponse> result = new ArrayList<>();
        for (CartItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product == null) continue; // 商品已删除则跳过

            CartItemResponse resp = new CartItemResponse();
            resp.setCartItemId(item.getId());
            resp.setProductId(product.getId());
            resp.setProductName(product.getName());
            resp.setProductImageUrl(product.getImageUrl());
            resp.setPrice(product.getPrice());
            resp.setQuantity(item.getQuantity());
            resp.setSweetness(item.getSweetness());
            resp.setTemperature(item.getTemperature());
            resp.setCupSize(item.getCupSize());
            resp.setAddOns(item.getAddOns());
            // 小计 = 单价 × 数量
            resp.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            result.add(resp);
        }
        return result;
    }

    @Override
    public void updateQuantity(Long id, Integer quantity) {
        CartItem item = cartItemMapper.selectById(id);
        if (item != null) {
            item.setQuantity(quantity);
            cartItemMapper.updateById(item);
        }
    }

    @Override
    public void remove(Long id) {
        cartItemMapper.deleteById(id);
    }
}

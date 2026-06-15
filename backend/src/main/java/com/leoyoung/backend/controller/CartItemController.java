package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.dto.CartAddRequest;
import com.leoyoung.backend.service.CartItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 购物车 Controller —— 处理用户的购物车增删改查
 */
@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    /** 加入购物车 */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody CartAddRequest request, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId"); // 从 JWT 拦截器中获取
        cartItemService.add(userId, request);
        return Result.success(null, "已加入购物车");
    }

    /** 查看购物车列表 */
    @GetMapping("/list")
    public Result<?> list(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return Result.success(cartItemService.list(userId));
    }

    /** 修改商品数量 */
    @PutMapping("/{id}")
    public Result<Void> updateQuantity(@PathVariable Long id,
                                       @RequestBody Map<String, Integer> body) {
        cartItemService.updateQuantity(id, body.get("quantity"));
        return Result.success(null, "数量已更新");
    }

    /** 删除购物车条目 */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        cartItemService.remove(id);
        return Result.success(null, "已从购物车移除");
    }
}

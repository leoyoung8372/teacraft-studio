package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品 Controller（用户端）—— 提供商品列表查询
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** 按分类查询商品（仅返回已上架） */
    @GetMapping
    public Result<?> list(@RequestParam Long categoryId) {
        return Result.success(productService.listByCategory(categoryId));
    }

    /** 查询商品详情 */
    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }
}

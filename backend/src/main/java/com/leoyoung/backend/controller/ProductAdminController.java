package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.dto.ProductSaveRequest;
import com.leoyoung.backend.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品管理 Controller（商家端）—— 提供商品的增删改查
 */
@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    /** 商品列表（分页 + 搜索） */
    @GetMapping
    public Result<?> list(@RequestParam(required = false) String keyword,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.listAll(keyword, page, size));
    }

    /** 新增/编辑商品（有 id 则更新，无则新增） */
    @PostMapping
    public Result<Void> save(@RequestBody ProductSaveRequest request) {
        productService.save(request);
        // 有 id 为编辑，无 id 为新增
        return Result.success(null, request.getId() != null ? "修改成功" : "新增成功");
    }

    /** 删除商品 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success(null, "已删除");
    }
}

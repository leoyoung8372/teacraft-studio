package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类 Controller —— 提供分类列表查询
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<?> list() {
        return Result.success(categoryService.list());
    }
}

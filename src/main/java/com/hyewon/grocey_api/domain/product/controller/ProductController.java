package com.hyewon.grocey_api.domain.product.controller;

import com.hyewon.grocey_api.domain.product.dto.ProductResponse;
import com.hyewon.grocey_api.domain.product.dto.ProductTabResponse;
import com.hyewon.grocey_api.domain.product.entity.TabType;
import com.hyewon.grocey_api.domain.product.service.ProductService;
import com.hyewon.grocey_api.domain.product.service.ProductTabService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductTabService productTabService;
    private final ProductService productService;

    @GetMapping
    public ProductTabResponse getProductsByTab(@RequestParam(defaultValue = "NEW") String tab) {
        TabType tabType = TabType.valueOf(tab.toUpperCase());
        return productTabService.getProductsByTab(tabType);
    }

    @GetMapping("/{productId}")
    public ProductResponse getProductDetail(@PathVariable Long productId) {
        return productService.getProductDetail(productId);
    }
}

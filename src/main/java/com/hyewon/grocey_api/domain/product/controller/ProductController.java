package com.hyewon.grocey_api.domain.product.controller;


import com.hyewon.grocey_api.domain.product.service.ProductService;
import com.hyewon.grocey_api.domain.product.service.ProductTabService;
import com.hyewon.grocey_api.domain.product.dto.ProductDto;
import com.hyewon.grocey_api.domain.product.entity.TabType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductTabService productTabService;
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProductsByTab(@RequestParam(defaultValue = "NEW") String tab) {
        TabType tabType = TabType.valueOf(tab.toUpperCase());
        return productTabService.getProductsByTab(tabType);
    }

    @GetMapping("/{productId}")
    public ProductDto getProductDetail(@PathVariable Long productId) {
        return productService.getProductDetail(productId);
    }
}

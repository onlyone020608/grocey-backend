package com.hyewon.grocey_api.domain.product;


import com.hyewon.grocey_api.domain.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductTabService productTabService;

    @GetMapping
    public List<ProductDto> getProductsByTab(@RequestParam(defaultValue = "NEW") String tab) {
        TabType tabType = TabType.valueOf(tab.toUpperCase());
        return productTabService.getProductsByTab(tabType);
    }
}

package com.hyewon.grocey_api.domain.product.service;

import com.hyewon.grocey_api.domain.product.dto.ProductResponse;
import com.hyewon.grocey_api.domain.product.dto.ProductTabResponse;
import com.hyewon.grocey_api.domain.product.entity.ProductTab;
import com.hyewon.grocey_api.domain.product.entity.TabType;
import com.hyewon.grocey_api.domain.product.repository.ProductTabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTabService {
    private final ProductTabRepository productTabRepository;

    @Cacheable(value = "productTabs", key = "#tab")
    @Transactional(readOnly = true)
    public ProductTabResponse getProductsByTab(TabType tab) {
        List<ProductTab> productTabs = productTabRepository.findByTabTypeWithProduct(tab);

        List<ProductResponse> responses = productTabs.stream()
                .map(productTab -> ProductResponse.from(productTab.getProduct()))
                .toList();

        return ProductTabResponse.from(responses);
    }
}

package com.hyewon.grocey_api.domain.product;

import com.hyewon.grocey_api.domain.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTabService {
    private final ProductTabRepository productTabRepository;

    public List<ProductDto> getProductsByTab(TabType tab) {
        List<ProductTab> productTabs = productTabRepository.findByTabType(tab);

        return productTabs.stream()
                .map(pt -> new ProductDto(pt.getProduct()))
                .toList();
    }

}

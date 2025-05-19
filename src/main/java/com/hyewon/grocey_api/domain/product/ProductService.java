package com.hyewon.grocey_api.domain.product;

import com.hyewon.grocey_api.domain.product.dto.ProductDto;
import com.hyewon.grocey_api.global.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductDto getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return new ProductDto(product);
    }
}

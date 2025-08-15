package com.hyewon.grocey_api.domain.product.service;

import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.repository.ProductRepository;
import com.hyewon.grocey_api.global.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional(readOnly = true)
    public List<Product> findRandomOnePerIngredient(List<Long> ingredientIds) {
        return productRepository.findRandomOneEachByIngredient(ingredientIds);
    }
}

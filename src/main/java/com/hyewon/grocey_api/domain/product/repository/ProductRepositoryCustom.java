package com.hyewon.grocey_api.domain.product.repository;

import com.hyewon.grocey_api.domain.product.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findRandomOneEachByIngredient(List<Long> ingredientIds);
}

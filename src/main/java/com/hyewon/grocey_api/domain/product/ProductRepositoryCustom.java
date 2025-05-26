package com.hyewon.grocey_api.domain.product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findRandomOneEachByIngredient(List<Long> ingredientIds);
}

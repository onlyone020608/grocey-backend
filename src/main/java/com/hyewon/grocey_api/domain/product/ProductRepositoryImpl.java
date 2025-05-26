package com.hyewon.grocey_api.domain.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    @Override
    public List<Product> findRandomOneEachByIngredient(List<Long> ingredientIds) {
        List<Product> all = queryFactory
                .selectFrom(product)
                .where(product.ingredient.id.in(ingredientIds))
                .fetch();

        Map<Long, List<Product>> grouped = all.stream()
                .collect(Collectors.groupingBy(p -> p.getIngredient().getId()));

        List<Product> result = new ArrayList<>();
        for (List<Product> group : grouped.values()) {
            Collections.shuffle(group);
            result.add(group.get(0));
        }

        return result;
    }
}

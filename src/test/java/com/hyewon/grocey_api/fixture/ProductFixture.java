package com.hyewon.grocey_api.fixture;

import com.hyewon.grocey_api.domain.product.entity.Product;

public class ProductFixture {

    public static Product aProduct() {
        return Product.builder()
                .id(1L)
                .name("Milk")
                .brand("SeoulDairy")
                .price(2000)
                .imageUrl("milk.png")
                .build();
    }
}

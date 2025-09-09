package com.hyewon.grocey_api.fixture;

import com.hyewon.grocey_api.domain.product.entity.Product;

public class ProductFixture {
    private Long id = 1L;
    private String name = "Milk";
    private String brand = "SeoulDairy";
    private int price = 2000;
    private String imageUrl = "milk.png";

    public static ProductFixture aProduct() {
        return new ProductFixture();
    }

    public ProductFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductFixture withName(String name) {
        this.name = name;
        return this;
    }

    public Product build() {
        return Product.builder()
                .id(id)
                .name(name)
                .brand(brand)
                .price(price)
                .imageUrl(imageUrl)
                .build();
    }
}

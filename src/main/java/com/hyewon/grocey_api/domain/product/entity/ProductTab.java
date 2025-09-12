package com.hyewon.grocey_api.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(
        name = "product_tab",
        indexes = {
                @Index(name = "idx_product_tab_tabtype", columnList = "tabType")
        }
)
public class ProductTab {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private TabType tabType;

    private ProductTab(Product product, TabType tabType) {
        this.product = product;
        this.tabType = tabType;
    }

    public static ProductTab of(Product product, TabType tabType) {
        return new ProductTab(product, tabType);
    }
}

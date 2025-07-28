package com.hyewon.grocey_api.domain.product.repository;

import com.hyewon.grocey_api.domain.product.entity.ProductTab;
import com.hyewon.grocey_api.domain.product.entity.TabType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTabRepository extends JpaRepository<ProductTab, Long> {
    List<ProductTab> findByTabType(TabType tabType);
}

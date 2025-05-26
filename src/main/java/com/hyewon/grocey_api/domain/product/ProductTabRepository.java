package com.hyewon.grocey_api.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTabRepository extends JpaRepository<ProductTab, Long> {
    List<ProductTab> findByTabType(TabType tabType);
}

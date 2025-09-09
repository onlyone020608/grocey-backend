package com.hyewon.grocey_api.domain.product.repository;

import com.hyewon.grocey_api.domain.product.entity.ProductTab;
import com.hyewon.grocey_api.domain.product.entity.TabType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductTabRepository extends JpaRepository<ProductTab, Long> {
    @Query("SELECT pt FROM ProductTab pt JOIN FETCH pt.product WHERE pt.tabType = :tabType")
    List<ProductTab> findByTabTypeWithProduct(@Param("tabType") TabType tabType);
}

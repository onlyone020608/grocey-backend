package com.hyewon.grocey_api.unit.domain.product;

import com.hyewon.grocey_api.domain.product.dto.ProductTabResponse;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.entity.ProductTab;
import com.hyewon.grocey_api.domain.product.entity.TabType;
import com.hyewon.grocey_api.domain.product.repository.ProductTabRepository;
import com.hyewon.grocey_api.domain.product.service.ProductTabService;
import com.hyewon.grocey_api.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductTabServiceTest {
    @Mock private ProductTabRepository productTabRepository;
    @InjectMocks private ProductTabService productTabService;

    private ProductTab productTab;

    @BeforeEach
    void setUp() {
        Product product = ProductFixture.aProduct().build();
        productTab = ProductTab.builder()
                .product(product)
                .tabType(TabType.BEST)
                .build();
    }

    @Test
    @DisplayName("returns product list when tab exists")
    void shouldReturnProductList_whenTabExists() {
        // given
        given(productTabRepository.findByTabTypeWithProduct(TabType.BEST)).willReturn(List.of(productTab));

        // when
        ProductTabResponse result = productTabService.getProductsByTab(TabType.BEST);

        // then
        assertThat(result.products()).hasSize(1);
        assertThat(result.products().get(0).productId()).isEqualTo(1L);
        assertThat(result.products().get(0).productName()).isEqualTo("Milk");
    }
}
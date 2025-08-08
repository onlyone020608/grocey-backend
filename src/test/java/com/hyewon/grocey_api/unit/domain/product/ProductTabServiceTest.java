package com.hyewon.grocey_api.unit.domain.product;

import com.hyewon.grocey_api.domain.product.dto.ProductResponse;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.entity.ProductTab;
import com.hyewon.grocey_api.domain.product.entity.TabType;
import com.hyewon.grocey_api.domain.product.repository.ProductTabRepository;
import com.hyewon.grocey_api.domain.product.service.ProductTabService;
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
    @Mock
    private ProductTabRepository productTabRepository;
    @InjectMocks
    private ProductTabService productTabService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Milk")
                .name("SeoulDairy")
                .price(2000)
                .imageUrl("milk.png")
                .build();
    }

    @Test
    @DisplayName("getProductsByTab - returns product list for given tab")
    void getProductsByTab_shouldReturnList() {
        // given
        ProductTab productTab = ProductTab.builder()
                .product(product)
                .tabType(TabType.BEST)
                .build();

        given(productTabRepository.findByTabType(TabType.BEST)).willReturn(List.of(productTab));

        // when
        List<ProductResponse> result = productTabService.getProductsByTab(TabType.BEST);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
        assertThat(result.get(0).getProductName()).isEqualTo("Milk");
    }
}
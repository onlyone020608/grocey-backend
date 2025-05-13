package com.hyewon.grocey_api.domain.product;

import com.hyewon.grocey_api.domain.product.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        product = new Product("Milk", "SeoulDairy", 2000, "milk.png");
        ReflectionTestUtils.setField(product, "id", 1L);
    }

    @Test
    @DisplayName("getProductsByTab - returns product list for given tab")
    void getProductsByTab_shouldReturnList() {
        // given
        TabType tab = TabType.BEST;
        ProductTab productTab = new ProductTab(product, tab);

        given(productTabRepository.findByTabType(tab)).willReturn(List.of(productTab));

        // when
        List<ProductDto> result = productTabService.getProductsByTab(tab);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
        assertThat(result.get(0).getProductName()).isEqualTo("Milk");
    }
}
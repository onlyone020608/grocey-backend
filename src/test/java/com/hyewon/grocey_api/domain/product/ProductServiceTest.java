package com.hyewon.grocey_api.domain.product;

import com.hyewon.grocey_api.domain.product.dto.ProductDto;
import com.hyewon.grocey_api.global.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Milk", "SeoulDairy", 2000, "milk.png");
        ReflectionTestUtils.setField(product, "id", 1L);
    }

    @Test
    @DisplayName("getProductDetail - returns product DTO by ID")
    void getProductDetail_shouldReturnProductDto() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        // when
        ProductDto result = productService.getProductDetail(1L);

        // then
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getProductName()).isEqualTo("Milk");
        assertThat(result.getBrandName()).isEqualTo("SeoulDairy");
        assertThat(result.getPrice()).isEqualTo(2000);
        assertThat(result.getImageUrl()).isEqualTo("milk.png");
    }

    @Test
    @DisplayName("getProductDetail - throws when product not found")
    void getProductDetail_shouldThrow_whenProductNotFound() {
        // given
        given(productRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductDetail(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found");
    }
}

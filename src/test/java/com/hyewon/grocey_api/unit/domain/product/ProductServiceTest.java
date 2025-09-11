package com.hyewon.grocey_api.unit.domain.product;

import com.hyewon.grocey_api.domain.product.dto.ProductResponse;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.repository.ProductRepository;
import com.hyewon.grocey_api.domain.product.service.ProductService;
import com.hyewon.grocey_api.fixture.ProductFixture;
import com.hyewon.grocey_api.global.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock private ProductRepository productRepository;
    @InjectMocks private ProductService productService;private Product product;

    @BeforeEach
    void setUp() {
        product = ProductFixture.aProduct().build();
    }

    @Test
    @DisplayName("returns product detail DTO when product exists")
    void shouldReturnProductDetailDto_whenProductExists() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        // when
        ProductResponse result = productService.getProductDetail(1L);

        // then
        assertThat(result.productName()).isEqualTo("Milk");
        assertThat(result.brandName()).isEqualTo("SeoulDairy");
        assertThat(result.price()).isEqualTo(2000);
        assertThat(result.imageUrl()).isEqualTo("milk.png");
    }

    @Test
    @DisplayName("throws ProductNotFoundException when product does not exist")
    void shouldThrowException_whenProductNotFound() {
        // given
        given(productRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThrows(ProductNotFoundException.class,
                () ->  productService.getProductDetail(999L));
    }
}

package com.hyewon.grocey_api.unit.domain.product;

import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.repository.ProductRepository;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.global.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductQueryServiceTest {
    @Mock ProductRepository productRepository;
    @InjectMocks ProductQueryService productQueryService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("getProduct - should return product")
    void getProduct_shouldSucceed() {
        // given
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(
                Optional.of(product));

        // when
        Product resultProduct = productQueryService.getProduct(productId);

        // then
        assertThat(resultProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("getProduct - should throw ProductNotFoundException when product does not exist")
    void getProduct_shouldThrowException_whenProductNotFound() {
        // given
        Long productId = 999L;
        given(productRepository.findById(productId)).willReturn(
                Optional.empty());

        // when & then
        assertThrows(ProductNotFoundException.class,
                () ->  productQueryService.getProduct(productId));
    }

    @Test
    @DisplayName("findRandomOnePerIngredient - should return random product per ingredient")
    void findRandomOnePerIngredient_shouldSucceed() {
        // given
        List<Long> ingredientIds = List.of(1L, 2L);
        given(productRepository.findRandomOneEachByIngredient(ingredientIds)).willReturn(
                List.of(product));

        // when
        List<Product> resultProducts = productQueryService.findRandomOnePerIngredient(ingredientIds);

        // then
        assertThat(resultProducts).isEqualTo(List.of(product));
    }
}

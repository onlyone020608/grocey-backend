package com.hyewon.grocey_api.unit.domain.product;

import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.repository.ProductRepository;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.fixture.ProductFixture;
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
        product = ProductFixture.aProduct();
    }

    @Test
    @DisplayName("returns product when it exists")
    void shouldReturnProduct_whenItExists() {
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
    @DisplayName("throws ProductNotFoundException when product does not exist")
    void shouldThrowException_whenProductNotFound() {
        // given
        Long productId = 999L;
        given(productRepository.findById(productId)).willReturn(
                Optional.empty());

        // when & then
        assertThrows(ProductNotFoundException.class,
                () ->  productQueryService.getProduct(productId));
    }

    @Test
    @DisplayName("returns random product per ingredient when ingredients exist")
    void shouldReturnRandomProductPerIngredient_whenIngredientsExist() {
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

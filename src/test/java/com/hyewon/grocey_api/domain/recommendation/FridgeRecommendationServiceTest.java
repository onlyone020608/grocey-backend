package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationDto;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeRecommendationServiceTest {

    @Mock
    private FridgeRecommendationRepository fridgeRecommendationRepository;
    @InjectMocks
    private FridgeRecommendationService fridgeRecommendationService;

    private Fridge fridge;
    private Product product;
    private FridgeRecommendation recommendation;

    @BeforeEach
    void setUp() {
        fridge = new Fridge(4.0, -18.0);
        ReflectionTestUtils.setField(fridge, "id", 1L);

        product = new Product("Milk", "SeoulDairy", 2000, "milk.png");
        ReflectionTestUtils.setField(product, "id", 10L);

        recommendation = new FridgeRecommendation(fridge);
        ReflectionTestUtils.setField(recommendation, "id", 100L);

        FridgeRecommendedProduct rp = new FridgeRecommendedProduct(product, recommendation);
        recommendation.getRecommendedProducts().add(rp);
    }

    @Test
    @DisplayName("getLatestRecommendation - returns latest recommendation for fridge")
    void getLatestRecommendation_shouldReturnDto() {
        // given
        given(fridgeRecommendationRepository.findTopByFridgeIdOrderByCreatedAtDesc(1L))
                .willReturn(Optional.of(recommendation));

        // when
        FridgeRecommendationDto result = fridgeRecommendationService.getLatestRecommendation(1L);

        // then
        assertThat(result.getRecommendationId()).isEqualTo(100L);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getProductId()).isEqualTo(10L);
        assertThat(result.getProducts().get(0).getProductName()).isEqualTo("Milk");
    }

    @Test
    @DisplayName("getLatestRecommendation - throws exception when recommendation not found")
    void getLatestRecommendation_shouldThrowIfNotFound() {
        // given
        given(fridgeRecommendationRepository.findTopByFridgeIdOrderByCreatedAtDesc(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> fridgeRecommendationService.getLatestRecommendation(1L))
                .isInstanceOf(RecommendationNotFoundException.class);
    }

}
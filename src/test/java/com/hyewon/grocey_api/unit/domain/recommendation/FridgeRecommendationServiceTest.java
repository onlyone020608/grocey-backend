package com.hyewon.grocey_api.unit.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendedProduct;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.service.FridgeRecommendationService;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeRecommendationServiceTest {

    @Mock
    private FridgeRecommendationRepository fridgeRecommendationRepository;
    @InjectMocks
    private FridgeRecommendationService fridgeRecommendationService;

    private Fridge fridge;
    private Product product;
    private FridgeRecommendation fridgeRecommendation;

    @BeforeEach
    void setUp() {
        fridge = Fridge.builder()
                .id(1L)
                .fridgeTemperature(4.0)
                .freezerTemperature(-18.0)
                .build();

        product = Product.builder()
                .id(10L)
                .productName("Milk")
                .brandName("SeoulDairy")
                .price(2000)
                .imageUrl("milk.png")
                .build();

        fridgeRecommendation = FridgeRecommendation.builder()
                .id(100L)
                .fridge(fridge)
                .build();

        FridgeRecommendedProduct recommendedProduct = FridgeRecommendedProduct.builder()
                .product(product)
                .fridgeRecommendation(fridgeRecommendation)
                .build();
        fridgeRecommendation.addRecommendationProduct(recommendedProduct);
    }

    @Test
    @DisplayName("getLatestRecommendation - returns latest recommendation for fridge")
    void getLatestRecommendation_shouldReturnDto() {
        // given
        given(fridgeRecommendationRepository.findTopByFridgeIdOrderByCreatedAtDesc(1L))
                .willReturn(Optional.of(fridgeRecommendation));

        // when
        FridgeRecommendationResponse result = fridgeRecommendationService.getLatestRecommendation(1L);

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
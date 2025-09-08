package com.hyewon.grocey_api.unit.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendedProduct;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendedProductRepository;
import com.hyewon.grocey_api.domain.recommendation.service.FridgeRecommendationService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.fixture.FridgeFixture;
import com.hyewon.grocey_api.fixture.ProductFixture;
import com.hyewon.grocey_api.fixture.UserFixture;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeRecommendationServiceTest {
    @Mock private FridgeRecommendationRepository fridgeRecommendationRepository;
    @Mock private FridgeRecommendedProductRepository fridgeRecommendedProductRepository;
    @Mock private FridgeQueryService fridgeQueryService;
    @Mock private ProductQueryService productQueryService;
    @Mock private RestTemplate restTemplate;
    @InjectMocks private FridgeRecommendationService fridgeRecommendationService;

    private Fridge fridge;
    private Product product;
    private FridgeRecommendation fridgeRecommendation;
    private User user;

    @BeforeEach
    void setUp() {
        fridge = FridgeFixture.aFridge();
        user = UserFixture.aDefaultUser();
        user.assignFridge(fridge);
        product = ProductFixture.aProduct();
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
        given(fridgeQueryService.getFridge(1L)).willReturn(fridge);

        List<Long> aiReturnedIds = List.of(1L, 2L, 3L);
        String url = "http://grocey-ai:5001/api/recommend/" + fridge.getUsers().get(0).getId();
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);

        given(productQueryService.findRandomOnePerIngredient(aiReturnedIds)).willReturn(List.of(product));
        given(fridgeRecommendationRepository.save(any(FridgeRecommendation.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        FridgeRecommendationResponse result = fridgeRecommendationService.getLatestRecommendation(1L);

        // then
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getProducts().get(0).getProductName()).isEqualTo("Milk");
    }

    @Test
    @DisplayName("getLatestRecommendation - throws exception when recommendation not found")
    void getLatestRecommendation_shouldThrowIfNotFound() {
        // given
        given(fridgeQueryService.getFridge(1L)).willReturn(fridge);

        List<Long> aiReturnedIds = List.of();
        String url = "http://grocey-ai:5001/api/recommend/" + fridge.getUsers().get(0).getId();
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);

        // when & then
        assertThatThrownBy(() -> fridgeRecommendationService.getLatestRecommendation(1L))
                .isInstanceOf(RecommendationNotFoundException.class);
    }
}
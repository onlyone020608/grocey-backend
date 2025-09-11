package com.hyewon.grocey_api.unit.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendationRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeRecommendationServiceTest {
    @Mock private FridgeRecommendationRepository fridgeRecommendationRepository;
    @Mock private FridgeQueryService fridgeQueryService;
    @Mock private ProductQueryService productQueryService;
    @Mock private RestTemplate restTemplate;
    @InjectMocks private FridgeRecommendationService fridgeRecommendationService;

    private Fridge fridge;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        fridge = FridgeFixture.aFridge();
        user = UserFixture.aDefaultUser();
        user.assignFridge(fridge);
        product = ProductFixture.aProduct().build();
    }

    @Test
    @DisplayName("returns latest recommendation for fridge when available")
    void shouldReturnLatestRecommendation_whenAvailable() {
        // given
        Long userId = 1L;
        given(fridgeQueryService.getFridgeByUserId(userId)).willReturn(fridge);

        List<Long> aiReturnedIds = List.of(1L, 2L, 3L);
        String url = "http://grocey-ai:5001/api/recommend/" + fridge.getUsers().get(0).getId();
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);

        given(productQueryService.findRandomOnePerIngredient(aiReturnedIds)).willReturn(List.of(product));
        given(fridgeRecommendationRepository.save(any(FridgeRecommendation.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        FridgeRecommendationResponse result = fridgeRecommendationService.getLatestRecommendation(userId);

        // then
        assertThat(result.products()).hasSize(1);
        assertThat(result.products().get(0).productId()).isEqualTo(1L);
        assertThat(result.products().get(0).productName()).isEqualTo("Milk");
    }

    @Test
    @DisplayName("throws RecommendationNotFoundException when no recommendation is available")
    void shouldThrowException_whenRecommendationNotFound() {
        // given
        Long userId = 1L;
        given(fridgeQueryService.getFridgeByUserId(userId)).willReturn(fridge);

        List<Long> aiReturnedIds = List.of();
        String url = "http://grocey-ai:5001/api/recommend/" + fridge.getUsers().get(0).getId();
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);

        // when & then
        assertThrows(RecommendationNotFoundException.class,
                () ->  fridgeRecommendationService.getLatestRecommendation(userId));
    }
}
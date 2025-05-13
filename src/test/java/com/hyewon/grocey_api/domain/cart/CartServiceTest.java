package com.hyewon.grocey_api.domain.cart;

import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.product.ProductRepository;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;


    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        Fridge fridge = new Fridge(4.0, -18.0);
        user = new User("tester", "test@email.com", "password", AgeGroup.TWENTIES, Gender.FEMALE);
        user.assignFridge(fridge);
        ReflectionTestUtils.setField(user, "id", 1L);

        product = new Product("Milk", "Seoul Daily", 2000, "image-url");
        ReflectionTestUtils.setField(product, "id", 1L);
    }

    @Test
    @DisplayName("addCartItem - creates new cart if none exists and adds item successfully")
    void addCartItem_shouldCreateNewCartAndAddItem() {
        // given
        AddCartItemRequest request = new AddCartItemRequest(1L, 3);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(cartRepository.findByUser(user)).willReturn(Optional.empty());
        given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<CartItem> cartItemCaptor = ArgumentCaptor.forClass(CartItem.class);

        // when
        cartService.addCartItem(1L, request);

        // then
        verify(cartItemRepository).save(cartItemCaptor.capture());
        CartItem savedItem = cartItemCaptor.getValue();

        assertThat(savedItem.getProduct()).isEqualTo(product);
        assertThat(savedItem.getQuantity()).isEqualTo(3);
        assertThat(savedItem.getCart()).isNotNull();
        assertThat(savedItem.getCart().getUser()).isEqualTo(user);
    }

}
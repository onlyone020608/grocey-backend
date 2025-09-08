package com.hyewon.grocey_api.unit.domain.cart;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.repository.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.service.CartItemService;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {
    @Mock private CartItemRepository cartItemRepository;
    @InjectMocks private CartItemService cartItemService;

    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        cartItem1 = CartItem.builder().build();
        cartItem2 = CartItem.builder().build();
    }

    @Test
    @DisplayName("returns cart items when they exist")
    void shouldReturnCartItems_whenTheyExist() {
        // given
        given(cartItemRepository.findAllById(List.of(1L,2L))).willReturn(
                List.of(cartItem1,cartItem2));

        // when
        List<CartItem> resultCartItems = cartItemService.getCartItems(List.of(1L, 2L));

        // then
        assertThat(resultCartItems).isEqualTo(List.of(cartItem1, cartItem2));
    }

    @Test
    @DisplayName("throws InvalidRequestException when cart items do not exist")
    void shouldThrowException_whenCartItemsNotFound() {
        // given
        given(cartItemRepository.findAllById(List.of(999L,888L))).willReturn(
                List.of());

        // when & then
        assertThrows(InvalidRequestException.class,
                () ->  cartItemService.getCartItems(List.of(999L,888L)));
    }

    @Test
    @DisplayName("deletes cart items successfully")
    void shouldDeleteCartItems_whenCalled() {
        // given
        List<CartItem> cartItems = List.of(
                cartItem1,
                cartItem2
        );

        // when
        cartItemService.deleteCartItems(cartItems);

        // then
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
    }
}

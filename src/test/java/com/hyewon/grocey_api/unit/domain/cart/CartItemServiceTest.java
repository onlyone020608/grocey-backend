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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
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
    @DisplayName("returns cart items with product when found")
    void shouldReturnCartItemsWithProduct_whenFound() {
        // given
        List<Long> ids = List.of(1L, 2L);
        Long userId = 100L;
        List<CartItem> expected = List.of(cartItem1, cartItem2);

        given(cartItemRepository.findAllByIdInAndUserIdWithProduct(ids, userId))
                .willReturn(expected);

        // when
        List<CartItem> result = cartItemService.getCartItemsWithProduct(ids, userId);

        // then
        assertThat(result).hasSize(2).containsExactlyInAnyOrder(cartItem1, cartItem2);
        verify(cartItemRepository, times(1))
                .findAllByIdInAndUserIdWithProduct(ids, userId);
    }

    @Test
    @DisplayName("throws InvalidRequestException when no cart items found")
    void shouldThrowException_whenNoCartItemsFound() {
        // given
        List<Long> ids = List.of(1L, 2L);
        Long userId = 100L;

        given(cartItemRepository.findAllByIdInAndUserIdWithProduct(ids, userId))
                .willReturn(List.of());

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> cartItemService.getCartItemsWithProduct(ids, userId));
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

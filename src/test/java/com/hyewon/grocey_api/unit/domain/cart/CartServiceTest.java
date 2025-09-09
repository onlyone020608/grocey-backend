package com.hyewon.grocey_api.unit.domain.cart;

import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.dto.CartResponse;
import com.hyewon.grocey_api.domain.cart.dto.UpdateCartItemRequest;
import com.hyewon.grocey_api.domain.cart.entity.Cart;
import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.repository.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.repository.CartRepository;
import com.hyewon.grocey_api.domain.cart.service.CartService;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.fixture.FridgeFixture;
import com.hyewon.grocey_api.fixture.ProductFixture;
import com.hyewon.grocey_api.fixture.UserFixture;
import com.hyewon.grocey_api.global.exception.CartItemNotFoundException;
import com.hyewon.grocey_api.global.exception.CartNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock private CartRepository cartRepository;
    @Mock private UserQueryService userQueryService;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductQueryService productQueryService;
    @InjectMocks private CartService cartService;

    private User user;
    private Product product1;
    private Product product2;
    private Cart cart;
    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        Fridge fridge = FridgeFixture.aFridge();
        user = UserFixture.aDefaultUser();
        user.assignFridge(fridge);
        product1 = ProductFixture.aProduct().withId(1L).withName("Milk").build();
        product2 = ProductFixture.aProduct().withId(2L).withName("Bread").build();
        cart = Cart.builder()
                .id(10L)
                .user(user)
                .fridge(fridge)
                .build();
        cartItem1 = CartItem.builder()
                .id(10L)
                .product(product1)
                .quantity(1)
                .build();
        cartItem2 = CartItem.builder()
                .id(20L)
                .product(product1)
                .quantity(2)
                .build();
    }

    @Test
    @DisplayName("returns cart with its items when cart exists for user")
    void shouldReturnCartWithItems_whenCartExistsForUser() {
        // given
        Long userId = 1L;

        cart.addCartItem(cartItem1);
        given(cartRepository.findByUserIdWithItemsAndProduct(userId)).willReturn(Optional.of(cart));

        // when
        CartResponse response = cartService.getCart(userId);

        // then
        assertThat(response.getCartId()).isEqualTo(10L);
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getProductName()).isEqualTo(product1.getName());
        assertThat(response.getItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("throws CartNotFoundException when user has no cart")
    void shouldThrowException_whenCartNotFoundForUser() {
        // given
        Long userId = 1L;

        given(cartRepository.findByUserIdWithItemsAndProduct(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCart(userId);
        });
    }

    @Test
    @DisplayName("creates new cart if none exists and adds item successfully")
    void shouldCreateNewCartAndAddItem_whenCartNotExists() {
        // given
        Long userId = 1L;
        AddCartItemRequest request = new AddCartItemRequest(1L, 3);
        ArgumentCaptor<CartItem> cartItemCaptor = ArgumentCaptor.forClass(CartItem.class);
        given(userQueryService.getUserById(userId)).willReturn(user);
        given(productQueryService.getProduct(1L)).willReturn(product1);
        given(cartRepository.findByUserId(userId)).willReturn(Optional.empty());
        given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        cartService.addCartItem(userId, request);

        // then
        verify(cartItemRepository).save(cartItemCaptor.capture());
        CartItem savedItem = cartItemCaptor.getValue();

        assertThat(savedItem.getProduct()).isEqualTo(product1);
        assertThat(savedItem.getQuantity()).isEqualTo(3);
        assertThat(savedItem.getCart()).isNotNull();
        assertThat(savedItem.getCart().getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("increases quantity if product already exists in cart")
    void shouldIncreaseQuantity_whenProductAlreadyExistsInCart() {
        // given
        Long userId = 1L;
        AddCartItemRequest request = new AddCartItemRequest(1L, 2);
        CartItem existingItem = CartItem.of(product1, 3);
        cart.addCartItem(existingItem);

        given(userQueryService.getUserById(userId)).willReturn(user);
        given(productQueryService.getProduct(1L)).willReturn(product1);
        given(cartRepository.findByUserId(userId)).willReturn(Optional.of(cart));
        given(cartItemRepository.findByCartIdAndProductId(10L, product1.getId())).willReturn(Optional.of(existingItem));

        // when
        cartService.addCartItem(userId, request);

        // then
        assertThat(existingItem.getQuantity()).isEqualTo(5);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    @DisplayName("updates cart item quantity successfully when user owns the item")
    void shouldUpdateCartItemQuantity_whenUserOwnsItem() {
        // given
        Long userId = 1L;
        int newQuantity = 5;

        UpdateCartItemRequest request = new UpdateCartItemRequest(10L, newQuantity);
        cart.addCartItem(cartItem1);

        given(cartItemRepository.findByIdAndUserId(10L, userId)).willReturn(Optional.of(cartItem1));

        // when
        cartService.updateCartItemQuantity(userId, request);

        // then
        assertThat(cartItem1.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    @DisplayName("throws CartItemNotFoundException when cart item is not found")
    void shouldThrowException_whenCartItemNotFound() {
        // given
        Long userId = 1L;
        Long cartItemId = 10L;

        UpdateCartItemRequest request = new UpdateCartItemRequest(cartItemId, 5);

        given(cartItemRepository.findByIdAndUserId(cartItemId, userId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(CartItemNotFoundException.class,
                () -> cartService.updateCartItemQuantity(userId, request));
    }

    @Test
    @DisplayName("removes multiple cart items when user owns them")
    void shouldRemoveCartItems_whenUserOwnsThem() {
        // given
        Long userId = 1L;

        cart.addCartItem(cartItem1);
        cart.addCartItem(cartItem2);

        given(cartRepository.findByUserId(userId)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllByIdInAndUserId(List.of(10L, 20L), userId)).willReturn(List.of(cartItem1, cartItem2));

        // when
        cartService.deleteCartItems(userId, List.of(10L, 20L));

        // then
        assertThat(cart.getCartItems()).doesNotContain(cartItem1, cartItem2);
    }

    @Test
    @DisplayName("throws AccessDeniedException when user tries to delete item not in their cart")
    void shouldThrowException_whenDeletingItemNotInUserCart() {
        // given
        Long userId = 1L;

        cart.addCartItem(cartItem1);

        given(cartRepository.findByUserId(userId)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllByIdInAndUserId(List.of(10L, 20L), userId))
                .willReturn(List.of(cartItem1));

        // when & then
        assertThrows(AccessDeniedException.class,
                () -> cartService.deleteCartItems(userId, List.of(10L, 20L)));
    }

    @Test
    @DisplayName("adds multiple new cart items in batch")
    void shouldAddMultipleNewCartItems_whenProductsNotExist() {
        // given
        Long userId = 1L;
        AddCartItemRequest req1 = new AddCartItemRequest(product1.getId(), 2);
        AddCartItemRequest req2 = new AddCartItemRequest(product2.getId(), 3);

        given(userQueryService.getUserById(userId)).willReturn(user);
        given(cartRepository.findByUserId(userId)).willReturn(Optional.of(cart));
        given(productQueryService.getProduct(product1.getId())).willReturn(product1);
        given(productQueryService.getProduct(product2.getId())).willReturn(product2);
        given(cartItemRepository.findByCartIdAndProductId(cart.getId(), product1.getId())).willReturn(Optional.empty());
        given(cartItemRepository.findByCartIdAndProductId(cart.getId(), product2.getId())).willReturn(Optional.empty());

        // when
        cartService.addCartItemsInBatch(userId, List.of(req1, req2));

        // then
        assertThat(cart.getCartItems()).hasSize(2);
        verify(cartItemRepository, times(2)).save(any(CartItem.class));
    }

    @Test
    @DisplayName("adds new items and increases quantity for existing items in one batch")
    void shouldAddAndUpdateItemsTogether_whenMixedBatch() {
        // given
        Long userId = 1L;
        CartItem existingItem = CartItem.of(product1, 5);
        cart.addCartItem(existingItem);

        AddCartItemRequest req1 = new AddCartItemRequest(product1.getId(), 2);
        AddCartItemRequest req2 = new AddCartItemRequest(product2.getId(), 3);

        given(userQueryService.getUserById(userId)).willReturn(user);
        given(cartRepository.findByUserId(userId)).willReturn(Optional.of(cart));
        given(productQueryService.getProduct(product1.getId())).willReturn(product1);
        given(productQueryService.getProduct(product2.getId())).willReturn(product2);
        given(cartItemRepository.findByCartIdAndProductId(cart.getId(), product1.getId())).willReturn(Optional.of(existingItem));
        given(cartItemRepository.findByCartIdAndProductId(cart.getId(), product2.getId())).willReturn(Optional.empty());

        // when
        cartService.addCartItemsInBatch(userId, List.of(req1, req2));

        // then
        assertThat(existingItem.getQuantity()).isEqualTo(7);
        assertThat(cart.getCartItems()).hasSize(2);
        verify(cartItemRepository).save(any(CartItem.class));
    }
}
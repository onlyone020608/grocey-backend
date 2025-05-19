package com.hyewon.grocey_api.domain.cart;

import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.dto.CartResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.UpdateCartItemRequest;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.product.ProductRepository;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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

    @Test
    @DisplayName("addCartItem - increases quantity if product already exists in cart")
    void addCartItem_shouldIncreaseQuantityIfProductExists() {
        // given
        AddCartItemRequest request = new AddCartItemRequest(1L, 2);

        // 기존 CartItem: 상품 동일, 수량 3
        CartItem existingItem = new CartItem(product, 3);
        Cart existingCart = new Cart(user, user.getFridge());
        existingCart.addCartItem(existingItem);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(cartRepository.findByUser(user)).willReturn(Optional.of(existingCart));

        // when
        cartService.addCartItem(1L, request);

        // then
        assertThat(existingItem.getQuantity()).isEqualTo(5); // 3 + 2
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    @DisplayName("deleteCartItems - removes multiple items when user owns them")
    void deleteCartItems_shouldRemoveItemsIfUserOwnsThem() {
        // given
        Long userId = 1L;
        ReflectionTestUtils.setField(user, "id", userId);

        Cart cart = new Cart(user, user.getFridge());
        ReflectionTestUtils.setField(cart, "id", 777L);

        CartItem item1 = new CartItem(product, 1);
        CartItem item2 = new CartItem(product, 2);
        cart.addCartItem(item1);
        cart.addCartItem(item2);

        ReflectionTestUtils.setField(item1, "id", 101L);
        ReflectionTestUtils.setField(item2, "id", 102L);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(cartRepository.findByUser(user)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllById(List.of(101L, 102L))).willReturn(List.of(item1, item2));

        // when
        cartService.deleteCartItems(userId, List.of(101L, 102L));

        // then
        assertThat(cart.getCartItems()).doesNotContain(item1, item2);
        verify(cartItemRepository).deleteAll(List.of(item1, item2));
    }

    @Test
    @DisplayName("deleteCartItems - throws AccessDeniedException when user does not own one of the items")
    void deleteCartItems_shouldThrowIfItemNotBelongsToUserCart() {
        // given
        Long attackerId = 999L;
        Long ownerId = 1L;

        User attacker = user;
        ReflectionTestUtils.setField(attacker, "id", attackerId);

        User owner = new User("owner", "owner@email.com", "pw", AgeGroup.TWENTIES, Gender.MALE);
        ReflectionTestUtils.setField(owner, "id", ownerId);

        Cart attackerCart = new Cart(attacker, attacker.getFridge());
        Cart ownerCart = new Cart(owner, owner.getFridge());
        ReflectionTestUtils.setField(attackerCart, "id", 888L);
        ReflectionTestUtils.setField(ownerCart, "id", 999L);

        CartItem item = new CartItem(product, 1);
        ReflectionTestUtils.setField(item, "id", 200L);
        ownerCart.addCartItem(item);

        given(userRepository.findById(attackerId)).willReturn(Optional.of(attacker));
        given(cartRepository.findByUser(attacker)).willReturn(Optional.of(attackerCart));
        given(cartItemRepository.findAllById(List.of(200L))).willReturn(List.of(item));

        // when & then
        assertThrows(AccessDeniedException.class, () -> {
            cartService.deleteCartItems(attackerId, List.of(200L));
        });
    }



    @Test
    @DisplayName("updateCartItemQuantity - updates quantity successfully when cart item belongs to user")
    void updateCartItemQuantity_shouldUpdateQuantityIfUserOwnsItem() {
        // given
        Long userId = 1L;
        Long cartItemId = 100L;
        int newQuantity = 5;

        UpdateCartItemRequest request = new UpdateCartItemRequest(cartItemId, newQuantity);

        Cart cart = new Cart(user, user.getFridge());
        ReflectionTestUtils.setField(user, "id", userId);
        CartItem cartItem = new CartItem(product, 2);
        cart.addCartItem(cartItem);
        ReflectionTestUtils.setField(cartItem, "id", cartItemId);

        given(cartItemRepository.findById(cartItemId)).willReturn(Optional.of(cartItem));

        // when
        cartService.updateCartItemQuantity(userId, request);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    @DisplayName("updateCartItemQuantity - throws AccessDeniedException if user does not own the cart item")
    void updateCartItemQuantity_shouldThrowIfUserDoesNotOwnItem() {
        // given
        Long actualOwnerId = 1L;
        Long attackerId = 999L; // 다른 사용자
        Long cartItemId = 100L;

        User anotherUser = new User("other", "other@email.com", "pass", AgeGroup.TWENTIES, Gender.MALE);
        ReflectionTestUtils.setField(anotherUser, "id", actualOwnerId);

        Cart cart = new Cart(anotherUser, anotherUser.getFridge());
        CartItem cartItem = new CartItem(product, 2);
        cart.addCartItem(cartItem);
        ReflectionTestUtils.setField(cartItem, "id", cartItemId);

        given(cartItemRepository.findById(cartItemId)).willReturn(Optional.of(cartItem));

        UpdateCartItemRequest request = new UpdateCartItemRequest(cartItemId, 5);

        // when & then
        assertThrows(AccessDeniedException.class, () -> {
            cartService.updateCartItemQuantity(attackerId, request);
        });
    }



    @Test
    @DisplayName("getCart - returns cart and its items for the user")
    void getCart_shouldReturnCartWithItems() {
        // given
        Long userId = 1L;
        ReflectionTestUtils.setField(user, "id", userId);

        Cart cart = new Cart(user, user.getFridge());
        ReflectionTestUtils.setField(cart, "id", 100L);

        CartItem item1 = new CartItem(product, 2);
        ReflectionTestUtils.setField(item1, "id", 10L);
        cart.addCartItem(item1);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(cartRepository.findByUser(user)).willReturn(Optional.of(cart));

        // when
        CartResponseDto response = cartService.getCart(userId);

        // then
        assertThat(response.getCartId()).isEqualTo(100L);
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getProductName()).isEqualTo(product.getProductName());
        assertThat(response.getItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("getCart - throws CartNotFoundException when cart does not exist for user")
    void getCart_shouldThrowWhenCartDoesNotExist() {
        // given
        Long userId = 1L;
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(cartRepository.findByUser(user)).willReturn(Optional.empty());

        // when & then
        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCart(userId);
        });
    }







}
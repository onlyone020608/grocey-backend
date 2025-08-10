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
import com.hyewon.grocey_api.domain.user.entity.AgeGroup;
import com.hyewon.grocey_api.domain.user.entity.Gender;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock private CartRepository cartRepository;
    @Mock private UserQueryService userQueryService;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductQueryService productQueryService;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        Fridge fridge = new Fridge(4.0, -18.0);

        user = User.builder()
                .id(1L)
                .username("tester")
                .email("test@email.com")
                .password("password")
                .ageGroup(AgeGroup.TWENTIES)
                .gender(Gender.FEMALE)
                .build();
        user.assignFridge(fridge);

        product = Product.builder()
                .id(1L)
                .name("Milk")
                .brand("Seoul Daily")
                .price(2000)
                .imageUrl("image-url")
                .build();

        cartItem1 = CartItem.builder()
                .id(10L)
                .product(product)
                .quantity(1)
                .build();

        cartItem2 = CartItem.builder()
                .id(20L)
                .product(product)
                .quantity(2)
                .build();
    }

    @Test
    @DisplayName("addCartItem - creates new cart if none exists and adds item successfully")
    void addCartItem_shouldCreateNewCartAndAddItem() {
        // given
        AddCartItemRequest request = new AddCartItemRequest(1L, 3);

        given(userQueryService.getUserById(1L)).willReturn(user);
        given(productQueryService.getProduct(1L)).willReturn(product);
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
        CartItem existingItem = CartItem.of(product, 3);
        Cart existingCart = Cart.of(user, user.getFridge());
        existingCart.addCartItem(existingItem);

        given(userQueryService.getUserById(1L)).willReturn(user);
        given(productQueryService.getProduct(1L)).willReturn(product);
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
        Cart cart = Cart.of(user, user.getFridge());
        ReflectionTestUtils.setField(cart, "id", 777L);

        cart.addCartItem(cartItem1);
        cart.addCartItem(cartItem2);

        given(userQueryService.getUserById(userId)).willReturn(user);
        given(cartRepository.findByUser(user)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllById(List.of(10L, 20L))).willReturn(List.of(cartItem1, cartItem2));

        // when
        cartService.deleteCartItems(userId, List.of(10L, 20L));

        // then
        assertThat(cart.getCartItems()).doesNotContain(cartItem1, cartItem2);
        verify(cartItemRepository).deleteAll(List.of(cartItem1, cartItem2));
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

        Cart attackerCart = Cart.of(attacker, attacker.getFridge());
        Cart ownerCart = Cart.of(owner, owner.getFridge());
        ReflectionTestUtils.setField(attackerCart, "id", 888L);
        ReflectionTestUtils.setField(ownerCart, "id", 999L);

        CartItem item = CartItem.of(product, 1);
        ReflectionTestUtils.setField(item, "id", 200L);
        ownerCart.addCartItem(item);

        given(userQueryService.getUserById(attackerId)).willReturn(user);
        given(cartRepository.findByUser(attacker)).willReturn(Optional.of(attackerCart));
        given(cartItemRepository.findAllById(List.of(200L))).willReturn(List.of(item));

        // when & then
        assertThrows(AccessDeniedException.class, () -> {
            cartService.deleteCartItems(attackerId, List.of(200L));
        });
    }

    @Test
    @DisplayName("deleteCartItems - throws AccessDeniedException when user does not own one of the items")
    void deleteCartItems_shouldThrowIfItemNotBelongsToUserCart2() {
        // given
        Long userId = 1L;

        Cart cart = Cart.builder()
                .id(20L)
                .user(user)
                .fridge(user.getFridge())
                .cartItems(new ArrayList<>())
                .build();

        Cart cart2 = Cart.builder()
                .id(30L)
                .build();

        cartItem1.assignCart(cart2);


        given(userQueryService.getUserById(userId)).willReturn(user);
        given(cartRepository.findByUser(user)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllById(List.of(20L))).willReturn(List.of(cartItem1));

        // when & then
        assertThrows(AccessDeniedException.class, () -> {
            cartService.deleteCartItems(userId, List.of(20L));
        });
    }

    @Test
    @DisplayName("updateCartItemQuantity - updates quantity successfully when cart item belongs to user")
    void updateCartItemQuantity_shouldUpdateQuantityIfUserOwnsItem() {
        // given
        Long userId = 1L;
        int newQuantity = 5;

        UpdateCartItemRequest request = new UpdateCartItemRequest(10L, newQuantity);

        Cart cart = Cart.of(user, user.getFridge());
        cart.addCartItem(cartItem1);

        given(cartItemRepository.findById(10L)).willReturn(Optional.of(cartItem1));

        // when
        cartService.updateCartItemQuantity(userId, request);

        // then
        assertThat(cartItem1.getQuantity()).isEqualTo(newQuantity);
    }


    @Test
    @DisplayName("updateCartItemQuantity - throws AccessDeniedException if user does not own the cart item")
    void updateCartItemQuantity_shouldThrowIfUserDoesNotOwnItem() {
        // given
        Long attackerId = 999L; // 다른 사용자
        Long cartItemId = 10L;

        Cart cart = Cart.builder()
                .user(user)
                .fridge(user.getFridge())
                .cartItems(new ArrayList<>())
                .build();

        cart.addCartItem(cartItem1);
        given(cartItemRepository.findById(cartItemId)).willReturn(Optional.of(cartItem1));

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
        Cart cart = Cart.builder()
               .id(10L)
               .user(user)
               .fridge(user.getFridge())
                .cartItems(new ArrayList<>())
                .build();

        cart.addCartItem(cartItem1);

        given(userQueryService.getUserById(1L)).willReturn(user);
        given(cartRepository.findByUser(user)).willReturn(Optional.of(cart));

        // when
        CartResponse response = cartService.getCart(userId);

        // then
        assertThat(response.getCartId()).isEqualTo(10L);
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getProductName()).isEqualTo(product.getName());
        assertThat(response.getItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("getCart - throws CartNotFoundException when cart does not exist for user")
    void getCart_shouldThrowWhenCartDoesNotExist() {
        // given
        Long userId = 1L;

        given(userQueryService.getUserById(1L)).willReturn(user);
        given(cartRepository.findByUser(user)).willReturn(Optional.empty());

        // when & then
        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCart(userId);
        });
    }

    @Test
    @DisplayName("addCartItemsInBatch - adds multiple items, creating cart if needed")
    void addCartItemsInBatch_shouldAddAllItemsCorrectly() {
        // given
        AddCartItemRequest request1 = new AddCartItemRequest(1L, 2);
        AddCartItemRequest request2 = new AddCartItemRequest(2L, 1);

        Product product1 = Product.builder()
                .id(1L)
                .name("Milk")
                .brand("BrandA")
                .price(1000)
                .imageUrl("image1")
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Bread")
                .brand("BrandB")
                .price(2000)
                .imageUrl("image2")
                .build();

        given(userQueryService.getUserById(1L)).willReturn(user);
        given(productQueryService.getProduct(1L)).willReturn(product1);
        given(productQueryService.getProduct(2L)).willReturn(product2);
        given(cartRepository.findByUser(user)).willReturn(Optional.empty());
        given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        cartService.addCartItemsInBatch(1L, List.of(request1, request2));

        // then
        verify(cartItemRepository).saveAll(anyList());
    }
}
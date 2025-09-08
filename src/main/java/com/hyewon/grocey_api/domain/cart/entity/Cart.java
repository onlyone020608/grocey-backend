package com.hyewon.grocey_api.domain.cart.entity;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id", nullable = false)
    private Fridge fridge;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    private Cart(User user, Fridge fridge) {
        this.user = user;
        this.fridge = fridge;
    }

    public static Cart of(User user, Fridge fridge) {
        Cart cart = new Cart(user, fridge);
        cart.cartItems = new ArrayList<>();
        return cart;
    }

    public void addCartItem(CartItem item) {
        cartItems.add(item);
        item.assignCart(this);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.removeFromCart();
    }
}

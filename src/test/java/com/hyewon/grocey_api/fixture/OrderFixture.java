package com.hyewon.grocey_api.fixture;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.PaymentMethod;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.user.entity.User;

import java.util.List;

public class OrderFixture {

    public static Order createOrder(User user, Product product, int quantity) {
        Order order = Order.of(user, "Gangnam-gu, Seoul", PaymentMethod.KAKAOPAY);
        CartItem cartItem = CartItem.of(product, quantity);
        order.addOrderItems(List.of(cartItem));
        return order;
    }
}

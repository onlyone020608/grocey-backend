package com.hyewon.grocey_api.domain.order.entity;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String address;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    private Order(User user, String address, PaymentMethod paymentMethod) {
        this.user = user;
        this.orderStatus = OrderStatus.CONFIRMED;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.orderItems = new ArrayList<>();
    }

    public static Order of(User user, String address, PaymentMethod paymentMethod) {
        return new Order(user, address, paymentMethod);
    }

    public void addOrderItems(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            OrderItem orderItem = OrderItem.of(this, item.getProduct(), item.getQuantity(), item.getProduct().getPrice());
            this.addItem(orderItem);
        }
    }

    private void addItem(OrderItem item) {
        orderItems.add(item);
        item.assignOrder(this);
    }
}

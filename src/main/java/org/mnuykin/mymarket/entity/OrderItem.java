package org.mnuykin.mymarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem  {
    @EmbeddedId
    private OrderItemKey orderItemKey;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("itemId")
    private Item item;

    private int quantity;
    private Long price;
}
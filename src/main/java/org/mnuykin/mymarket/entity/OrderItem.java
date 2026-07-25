package org.mnuykin.mymarket.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
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
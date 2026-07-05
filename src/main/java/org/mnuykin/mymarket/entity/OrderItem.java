package org.mnuykin.mymarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table
public class OrderItem  {
    @EmbeddedId
    private OrderItemKey orderItemKey;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    @ManyToOne
    private Item item;

    private int quantity;
    private BigDecimal price;
}
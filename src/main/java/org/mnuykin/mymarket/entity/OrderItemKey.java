package org.mnuykin.mymarket.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class OrderItemKey{
    private Long orderId;
    private Long itemId;
}

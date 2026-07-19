package org.mnuykin.mymarket.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemKey{
    private Long orderId;
    private Long itemId;
}
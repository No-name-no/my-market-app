package org.mnuykin.mymarket.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemKey implements Serializable {
    private Long orderId;
    private Long itemId;
}
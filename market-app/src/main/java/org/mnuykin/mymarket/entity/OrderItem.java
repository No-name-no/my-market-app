package org.mnuykin.mymarket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem  {
    @Column("order_id")
    private Long orderId;
    @Column("item_id")
    private Long itemId;
    @Column("quantity")
    private int quantity;
    @Column("price")
    private Long price;
}
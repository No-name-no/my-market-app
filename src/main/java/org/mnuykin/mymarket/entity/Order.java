package org.mnuykin.mymarket.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @Column("id")
    private Long id;

    @Column("total_sum")
    private Long totalSum;
}

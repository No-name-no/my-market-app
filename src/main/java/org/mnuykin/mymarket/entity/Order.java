package org.mnuykin.mymarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long totalSum;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<OrderItem> items;
}

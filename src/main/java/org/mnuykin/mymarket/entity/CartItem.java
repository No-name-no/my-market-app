package org.mnuykin.mymarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(nullable = false)
    private Item item;

    @Column(nullable = false)
    @NotNull
    private Integer count = 1;

    public void addItem(){
        if (count < Integer.MAX_VALUE) {
            count++;
        }
    }

    public void deleteItem(){
        if (count > 0) {
            count--;
        }
    }
}
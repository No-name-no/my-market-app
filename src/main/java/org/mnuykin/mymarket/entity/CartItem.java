package org.mnuykin.mymarket.entity;

import jakarta.persistence.*;
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

    @JoinColumn(nullable = false)
    private Integer count;

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
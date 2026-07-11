package org.mnuykin.mymarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String title;
    private String description;
    private String imgPath;
    private Long price;
    private Integer count = 0;

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

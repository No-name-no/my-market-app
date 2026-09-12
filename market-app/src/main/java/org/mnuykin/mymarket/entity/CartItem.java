package org.mnuykin.mymarket.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItem {
    @Id
    @Column("id")
    Long id;

    @Column("item_id")
    private Long itemId;

    @Column("count")
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
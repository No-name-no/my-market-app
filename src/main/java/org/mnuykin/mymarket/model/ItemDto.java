package org.mnuykin.mymarket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemDto {
    Long id;
    String title;
    String description;
    String imgPath;
    Long price;
    int count;

    public static ItemDto mockItem(){
        return new ItemDto(-1L, null, null, null, 0L, 0);
    }
}
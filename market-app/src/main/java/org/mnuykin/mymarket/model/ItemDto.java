package org.mnuykin.mymarket.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
package org.mnuykin.mymarket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {
    private Long id;
    private String title;
    private String description;
    private String imgPath;
    private Long price;
    private int count;
}
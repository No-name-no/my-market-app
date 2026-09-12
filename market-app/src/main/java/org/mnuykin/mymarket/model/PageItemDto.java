package org.mnuykin.mymarket.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class PageItemDto {
    private List<ItemDto> content;
    private int size;
    private int number;
    private boolean hasPrevious;
    private boolean hasNex;
    private long total;
}
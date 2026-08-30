package org.mnuykin.mymarket.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class OrderDto {
    private long id;
    private long totalSum;
    private List<ItemDto> items;
}
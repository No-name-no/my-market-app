package org.mnuykin.mymarket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {
    private long id;
    private long totalSum;
    private List<ItemDto> items;
}
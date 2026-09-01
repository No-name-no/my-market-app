package org.mnuykin.mymarket.repository.dto;

public record CartItemData(
        Long id, Long item, Integer count, Long price
) {
}

package org.mnuykin.mymarket.repository.dto;

public record CartItemData(
        Long id, Long itemId, Integer count, Long price
) {
}

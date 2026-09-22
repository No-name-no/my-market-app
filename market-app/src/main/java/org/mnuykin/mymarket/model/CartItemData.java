package org.mnuykin.mymarket.model;

public record CartItemData(
        Long id, Long item, Integer count, Long price
) {
}

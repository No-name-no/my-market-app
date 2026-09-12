package org.mnuykin.mymarket.model;

import lombok.Getter;

@Getter
public enum ItemsSort {
    NO,
    ALPHA,
    PRICE;

    public static final String DEFAULT = "NO";
}

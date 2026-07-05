package org.mnuykin.mymarket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Paging {
    private int pageSize;
    private int pageNumber;
    private boolean hasPrevious;
    private boolean hasNex;
}

package org.mnuykin.mymarket.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class PagingDto {
    private int pageSize;
    private int pageNumber;
    private boolean hasPrevious;
    private boolean hasNext;
}
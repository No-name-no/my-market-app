package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface ItemService {
    Mono<Page<ItemDto>> findItems(String search, ItemsSort itemsSort, Integer pageNumber, Integer pageSize);
    Mono<ItemDto> getItemById(Long id);
}

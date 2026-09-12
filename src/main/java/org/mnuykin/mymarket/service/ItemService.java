package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.springframework.data.domain.Page;

public interface ItemService {
    Page<ItemDto> findItems(String search, ItemsSort itemsSort, Integer pageNumber, Integer pageSize);
    ItemDto getItemById(Long id);
}

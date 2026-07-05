package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.Item;
import org.mnuykin.mymarket.model.ItemsSort;
import org.springframework.data.domain.Page;

public interface ItemService {
    Page<Item> findItems(String search, ItemsSort itemsSort, Integer pageNumber, Integer pageSize);
    Item getItemById(Long id);
}

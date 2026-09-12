package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemAction;

import java.util.List;

public interface CartService {
    void executeAction(Long id, ItemAction action);
    List<ItemDto> getItems();
    long getTotal();
}

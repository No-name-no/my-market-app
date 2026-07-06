package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public void executeAction(Long id, ItemAction action) {

    }

    @Override
    public List<ItemDto> getItems() {
        return List.of();
    }

    @Override
    public long getTotal() {
        return 0;
    }
}

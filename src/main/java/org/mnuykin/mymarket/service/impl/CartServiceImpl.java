package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {
    final private ItemRepository itemRepository;
    final private ItemMapper itemMapper;

    @Autowired
    CartServiceImpl(ItemRepository itemRepository,
                    ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public void executeAction(Long id, ItemAction action) {
        Item item = itemRepository.getItemById(id).orElseThrow();
        switch (action){
            case PLUS -> item.addItem();
            case MINUS -> item.deleteItem();
            case DELETE -> item.setCount(0);
        }
        itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems() {
        return itemMapper.toDtoList(itemRepository.findByCountGreaterThan(0));
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotal() {
        return Objects.requireNonNullElse(itemRepository.getCartTotal(), 0L);
    }
}

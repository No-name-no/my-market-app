package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.entity.CartItem;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.repository.CartRepository;
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
    final private CartRepository cartRepository;
    final private ItemMapper itemMapper;

    @Autowired
    CartServiceImpl(ItemRepository itemRepository,
                    CartRepository cartRepository,
                    ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public void executeAction(Long id, ItemAction action) {
        Item item = itemRepository.getItemById(id).orElseThrow(() -> new NotFoundException(id));
        CartItem cartItem = cartRepository.getCartItemByItem_Id(item.getId()).orElse(new CartItem(null, item, 0));

        switch (action){
            case PLUS -> cartItem.addItem();
            case MINUS -> cartItem.deleteItem();
            case DELETE -> cartItem.setCount(0);
        }
        if (cartItem.getCount() == 0){
            cartRepository.delete(cartItem);
            return;
        }

        cartRepository.save(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems() {
        return itemMapper.toDtoList(cartRepository.findAll().stream().map(cartItem -> {
            Item item = cartItem.getItem();
            item.setCount(cartItem.getCount());
            return item;
        }).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotal() {
        return Objects.requireNonNullElse(cartRepository.getCartTotal(), 0L);
    }
}

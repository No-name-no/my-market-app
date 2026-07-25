package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.entity.CartItem;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.repository.CartRepository;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    final private ItemRepository itemRepository;
    final private CartRepository cartRepository;
    final private ItemMapper itemMapper;

    @Autowired
    ItemServiceImpl(ItemRepository itemRepository,
                    CartRepository cartRepository,
                    ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDto> findItems(String search, ItemsSort itemsSort, Integer pageNumber, Integer pageSize) {
        final Sort sort = switch (itemsSort){
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
            default -> Sort.unsorted();
        };
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        List<CartItem> cartItems = cartRepository.findAll();

        Page<Item> page;
        if (StringUtils.hasText(search)){
            page = itemRepository.findByDescriptionContainsIgnoreCaseOrTitleContainsIgnoreCase(search, search, pageable);
        } else {
            page = itemRepository.findAll(pageable);
        }

        return page.map((item) -> {
            //Переделать как-то нормально :)
            for(CartItem cartItem : cartItems){
                if (cartItem.getItem().equals(item)){
                    item.setCount(cartItem.getCount());
                    break;
                }
            }

            return itemMapper.toDto(item);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.getItemById(id).orElseThrow(() -> new NotFoundException(id));

        List<CartItem> cartItems = cartRepository.findAll();
        for(CartItem cartItem : cartItems){
            if (cartItem.getItem().equals(item)){
                item.setCount(cartItem.getCount());
                break;
            }
        }

        return itemMapper.toDto(item);
    }
}
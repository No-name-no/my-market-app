package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
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

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    final private ItemRepository itemRepository;
    final private ItemMapper itemMapper;

    @Autowired
    ItemServiceImpl(ItemRepository itemRepository,
                    ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDto> findItems(String search, ItemsSort itemsSort, Integer pageNumber, Integer pageSize) {
        final Sort sort = switch (itemsSort){
            case NO -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
            default -> Sort.unsorted();
        };
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Item> page;
        if (StringUtils.hasText(search)){
            page = itemRepository.findByDescriptionLikeOrTitleLike(search, search, pageable);
        } else {
            page = itemRepository.findAll(pageable);
        }

        return page.map(itemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long id) {
        Optional<Item> item = itemRepository.getItemById(id);
        return itemMapper.toDto(item.orElseThrow());
    }
}

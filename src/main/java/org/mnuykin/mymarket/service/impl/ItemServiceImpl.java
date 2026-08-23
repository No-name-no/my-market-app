package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.repository.CartRepository;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Page<ItemDto>> findItems(String search, ItemsSort itemsSort, Integer pageNumber, Integer pageSize) {
        final Sort sort = switch (itemsSort){
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
            default -> Sort.unsorted();
        };
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Flux<Item> itemFlux = StringUtils.hasText(search)
                ? itemRepository.findByDescriptionContainsIgnoreCaseOrTitleContainsIgnoreCase(search, search, pageable)
                : itemRepository.findAllBy(pageable);

        Flux<ItemDto> itemDtoFlux = itemFlux
                .flatMap(item ->
                cartRepository.getCartItemByItemId(item.getId())
                        .map(cartItem -> itemMapper.toDto(item, cartItem.getCount()))
                        .switchIfEmpty(Mono.just(itemMapper.toDto(item, 0)))
        );

        return itemDtoFlux.collectList().zipWith(this.itemRepository.count()).map(
                p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ItemDto> getItemById(Long id) {
        return itemRepository.getItemById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(id)))
                .flatMap(item ->
                        cartRepository.getCartItemByItemId(item.getId())
                                .map(cartItem -> itemMapper.toDto(item, cartItem.getCount()))
                );
    }
}
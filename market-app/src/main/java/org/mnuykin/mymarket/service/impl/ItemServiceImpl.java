package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.model.PageItemDto;
import org.mnuykin.mymarket.repository.CartRepository;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.CacheService;
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
    final private CacheService cacheService;

    @Autowired
    ItemServiceImpl(ItemRepository itemRepository,
                    CartRepository cartRepository,
                    CacheService cacheService,
                    ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.cacheService = cacheService;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PageItemDto> findItems(String search, ItemsSort itemsSort, Integer pageNumber, Integer pageSize) {
        final String cacheKey = String.format("items:search=%s:itemsSort=%s:pageNumber=%d:pageSize=%d",
                search, itemsSort.name(), pageNumber, pageSize
        );

        final Sort sort = switch (itemsSort){
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
            default -> Sort.unsorted();
        };
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        final boolean byTextSearch = StringUtils.hasText(search);
        Flux<Item> itemFlux = byTextSearch
                ? itemRepository.findByDescriptionContainsIgnoreCaseOrTitleContainsIgnoreCase(search, search, pageable)
                : itemRepository.findAllBy(pageable);
        Mono<Long> countMono = byTextSearch
                ? itemRepository.countByDescriptionContainsIgnoreCaseOrTitleContainsIgnoreCase(search, search)
                : itemRepository.count();

        return cacheService.getObjs(cacheKey)
                .cast(Item.class).flatMap(this::getItemDtoWithDataCard).collectList()
                .filter(itemDtos -> !itemDtos.isEmpty())
                .switchIfEmpty(
                        itemFlux.collectList()
                                .flatMap(list -> cacheService.save(cacheKey, list).thenReturn(list))
                                .flatMapMany(Flux::fromIterable).flatMap(this::getItemDtoWithDataCard).collectList()
                )
                .zipWith(countMono)
                .map(p -> new PageItemDto(
                        p.getT1(),
                        pageable.getPageSize(),
                        pageable.getPageNumber(),
                        pageable.hasPrevious(),
                        pageable.getPageNumber() + 1 < p.getT2(),
                        p.getT2()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ItemDto> getItemById(Long id) {
        final String cacheKey = String.format("item:id=%d", id);

        return cacheService.get(cacheKey)
                .cast(Item.class)
                .flatMap(this::getItemDtoWithDataCard)
                .switchIfEmpty(
                        itemRepository.getItemById(id)
                        .switchIfEmpty(Mono.error(new NotFoundException(id)))
                        .flatMap(item -> cacheService.save(cacheKey, item)
                                .thenReturn(item)
                        ).flatMap(this::getItemDtoWithDataCard)
                );
    }

    private Mono<ItemDto> getItemDtoWithDataCard(Item item){
        return cartRepository.getCartItemByItemId(item.getId())
                .map(cartItem -> itemMapper.toDto(item, cartItem.getCount()))
                .switchIfEmpty(Mono.just(itemMapper.toDto(item, 0)));
    }
}
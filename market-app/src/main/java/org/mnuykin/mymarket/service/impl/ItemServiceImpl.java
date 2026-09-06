package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.config.CacheConfig;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.model.PageItemDto;
import org.mnuykin.mymarket.repository.CartRepository;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
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
    final private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Autowired
    ItemServiceImpl(ItemRepository itemRepository,
                    CartRepository cartRepository,
                    ReactiveRedisTemplate<String, Object> reactiveRedisTemplate,
                    ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
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

        return reactiveRedisTemplate.opsForList().range(cacheKey, 0, -1)
                .cast(Item.class).flatMap(this::getItemDtoWithDataCard).collectList()
                .filter(itemDtos -> !itemDtos.isEmpty())
                .switchIfEmpty(
                        itemFlux.collectList()
                                .flatMap(list -> reactiveRedisTemplate.opsForList()
                                    .rightPushAll(cacheKey, list.toArray())
                                    .then(reactiveRedisTemplate.expire(cacheKey, CacheConfig.CACHE_TTL))
                                    .thenReturn(list))
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

        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .cast(Item.class)
                .flatMap(this::getItemDtoWithDataCard)
                .switchIfEmpty(
                        itemRepository.getItemById(id)
                        .switchIfEmpty(Mono.error(new NotFoundException(id)))
                        .flatMap(item -> reactiveRedisTemplate
                                .opsForValue()
                                .set(cacheKey, item, CacheConfig.CACHE_TTL)
                                .doOnSuccess(aBoolean -> System.out.println(" Cached item id=" + id))
                                .doOnError(aBoolean -> System.out.println(" Failed to cache item id=" + id))
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
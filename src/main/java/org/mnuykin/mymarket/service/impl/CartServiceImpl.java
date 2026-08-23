package org.mnuykin.mymarket.service.impl;

import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.mnuykin.mymarket.entity.CartItem;
import org.mnuykin.mymarket.mapper.ItemMapper;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.repository.CartRepository;
import org.mnuykin.mymarket.repository.ItemRepository;
import org.mnuykin.mymarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Void> executeAction(Long id, ItemAction action){
        return itemRepository.getItemById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(id)))
                .flatMap(item -> {
                    return cartRepository.getCartItemByItemId(item.getId())
                            .defaultIfEmpty(new CartItem(null, item.getId(), 0))
                            .flatMap(cartItem -> {
                                switch (action){
                                    case PLUS -> cartItem.addItem();
                                    case MINUS -> cartItem.deleteItem();
                                    case DELETE -> cartItem.setCount(0);
                                }
                                if (cartItem.getCount() == 0){
                                    return cartRepository.delete(cartItem);
                                } else {
                                    return cartRepository.save(cartItem).then();
                                }
                            });
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ItemDto> getItems(){
        return cartRepository.findAll().flatMap(
                cartItem -> itemRepository.getItemById(cartItem.getId())
                        .map(item -> itemMapper.toDto(item, cartItem.getCount()))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Long> getTotal() {
        return cartRepository.getCartTotal().switchIfEmpty(Mono.just(0L));
    }
}

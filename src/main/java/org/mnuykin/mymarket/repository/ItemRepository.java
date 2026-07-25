package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {
    Mono<Item> getItemById(Long id);
    Flux<Item> findAllBy(Pageable pageable);
    Flux<Item> findByDescriptionContainsIgnoreCaseOrTitleContainsIgnoreCase(String description, String description1, Pageable pageable);
}
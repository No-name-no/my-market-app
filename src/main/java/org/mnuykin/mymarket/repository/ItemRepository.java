package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> getItemById(Long id);

    Page<Item> findByDescriptionLikeOrTitleLike(String description, String title, Sort sort, Pageable pageable);
}
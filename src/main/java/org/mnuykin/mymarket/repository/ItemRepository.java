package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> getItemById(Long id);
    Page<Item> findByDescriptionLikeOrTitleLike(String description, String title, Pageable pageable);
    List<Item> findByCountGreaterThan(Integer count);

    @Query("Select sum(i.price) From Item i Where i.count > 0")
    Long getCartTotal();

    @Modifying
    @Query("Update Item i SET i.count = 0 WHERE i.count > 0")
    void clearCart();
}
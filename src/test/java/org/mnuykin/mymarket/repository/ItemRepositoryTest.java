package org.mnuykin.mymarket.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class ItemRepositoryTest {

    private static final String NON_EXISTENT_PATTERN = "%несуществующее%";
    private static final Integer COUNT_THRESHOLD = 0;
    private static final Long NON_EXISTING_ID = 999L;

    @Autowired
    private ItemRepository itemRepository;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = new Item();
        item1.setTitle("Телефон");
        item1.setDescription("Смартфон с хорошей камерой");
        item1.setPrice(30000L);
        item1.setCount(5);

        item2 = new Item();
        item2.setTitle("Наушники");
        item2.setDescription("Беспроводные наушники");
        item2.setPrice(5000L);
        item2.setCount(0);

        Item item3 = new Item();
        item3.setTitle("Чехол");
        item3.setDescription("Силиконовый чехол для телефона");
        item3.setPrice(1000L);
        item3.setCount(10);

        Item item4 = new Item();
        item4.setTitle("Зарядное устройство");
        item4.setDescription("Быстрая зарядка");
        item4.setPrice(2000L);
        item4.setCount(3);

        itemRepository.saveAll(List.of(item1, item2, item3, item4));
    }

    @Test
    void getItemById_ShouldReturnItem_WhenIdExists() {
        Long existingId = item1.getId();
        Optional<Item> found = itemRepository.getItemById(existingId);
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Телефон");
        assertThat(found.get().getPrice()).isEqualTo(30000L);
    }

    @Test
    void getItemById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {
        Optional<Item> found = itemRepository.getItemById(NON_EXISTING_ID);
        assertThat(found).isEmpty();
    }

    @Test
    void findByDescriptionLikeOrTitleLike_ShouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> page = itemRepository.findByDescriptionContainsIgnoreCaseAndDescriptionContainsIgnoreCase(NON_EXISTENT_PATTERN, NON_EXISTENT_PATTERN, pageable);
        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
    }

    /*@Test
    void getCartTotal_ShouldReturnSumOfPricesOfItemsWithPositiveCount() {
        Long total = itemRepository.getCartTotal();
        assertThat(total).isEqualTo(43000L);
    }

    @Test
    void clearCart_ShouldNotAffectItemsWithCountZero() {
        Long id2 = item2.getId();
        itemRepository.clearCart();
        Optional<Item> item = itemRepository.findById(id2);
        assertThat(item).isPresent();
        assertThat(item.get().getCount()).isZero();
    }*/
}
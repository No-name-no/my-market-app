package org.mnuykin.mymarket.cache;

import org.junit.jupiter.api.Test;
import org.mnuykin.mymarket.entity.Item;
import org.mnuykin.mymarket.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ImportTestcontainers(RedisTestContainer.class)
public class CacheServiceTest extends RedisTestContainer{
    @Autowired
    CacheService cacheService;

    @MockitoBean
    protected ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    @MockitoBean
    protected ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager;

    @Test
    void test(){
        final String key = "item:id=1";
        Item item = new Item(1L, "title", "des", "path", 1L);

        final String keyList = "items:ids=1";
        List<Item> listItem = new ArrayList<>();
        listItem.add(item);

        assertEquals(true, cacheService.save(key, item).block());
        assertEquals(true, cacheService.save(keyList, listItem).block());
        assertEquals(item.getId(), Objects.requireNonNull(cacheService.get(key).cast(Item.class).block()).getId());
        assertEquals(item.getId(), Objects.requireNonNull(cacheService.getObjs(keyList).cast(Item.class).blockFirst()).getId());
    }
}
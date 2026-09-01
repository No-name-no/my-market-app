package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.service.CartService;
import org.mnuykin.mymarket.service.ItemService;
import org.mnuykin.mymarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest({CartController.class, ItemController.class, OrderController.class})
public abstract class BaseControllerTest {
    @Autowired
    protected WebTestClient webTestClient;

    @MockitoBean
    protected CartService cartService;

    @MockitoBean
    protected ItemService itemService;

    @MockitoBean
    protected OrderService orderService;
}

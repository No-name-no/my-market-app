package org.mnuykin.mymarket.controller;

import lombok.extern.slf4j.Slf4j;
import org.mnuykin.mymarket.config.security.SecurityContextUtils;
import org.mnuykin.mymarket.model.OrderDto;
import org.mnuykin.mymarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Controller
public class OrderController {
    final private OrderService orderService;

    @Autowired
    public OrderController (OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public Mono<String> getOrders(Model model) {
        Mono<List<OrderDto>> ordersMono = orderService.getOrder().collectList();
        Mono<Boolean> authMono = SecurityContextUtils.isAuthenticated();

        return Mono.zip(ordersMono, authMono)
                .doOnNext(tuple -> {
                    model.addAttribute("orders", tuple.getT1());
                    model.addAttribute("isAuthenticated", tuple.getT2());
                })
                .thenReturn("orders");
    }

    @GetMapping("/orders/{id}")
    public Mono<String> getOrder(@PathVariable Long id,
                                 @RequestParam(defaultValue = "false") boolean newOrder,
                                 Model model) {
        Mono<OrderDto> orderMono = orderService.getOrderById(id);
        Mono<Boolean> authMono = SecurityContextUtils.isAuthenticated();

        return Mono.zip(orderMono, authMono)
                .doOnNext(tuple -> {
                    model.addAttribute("order", tuple.getT1());
                    model.addAttribute("newOrder", newOrder);
                    model.addAttribute("isAuthenticated", tuple.getT2());
                })
                .thenReturn("order");
    }

    @PostMapping("/buy")
    public Mono<Rendering> buy() {
        return orderService.create()
                .map(order -> Rendering.redirectTo(
                        UriComponentsBuilder
                                .fromPath("/orders/" +  order.getId())
                                .queryParam("newOrder", true)
                                .build().toUri().toString()
                        ).build())
                .onErrorResume(e -> {
                    log.error("Failed to create order", e);
                    return Mono.just(Rendering.redirectTo("/cart/items").build());
                });
    }
}

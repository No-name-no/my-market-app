package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class OrderController {
    final private OrderService orderService;

    @Autowired
    public OrderController (OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public Mono<String> getOrders (Model model){
        return orderService.getOrder()
                .collectList()
                .doOnNext(
                        orderDtos -> model.addAttribute("orders", orderDtos))
                .thenReturn("orders");
    }

    @GetMapping("/orders/{id}")
    public Mono<String> getOrder(@PathVariable Long id,
                    @RequestParam(defaultValue = "false") boolean newOrder,
                    Model model) {
        return orderService.getOrderById(id)
                .doOnNext(orderDto -> {
                    model.addAttribute("order", orderDto);
                    model.addAttribute("newOrder", newOrder);
                }).thenReturn("order");
    }

    @PostMapping("/buy")
    public Mono<Rendering> buy() {
        return orderService.create()
                .map(order -> Rendering.redirectTo("/orders/{id}")
                        .modelAttribute("id", order.getId())
                        .modelAttribute("newOrder", true)
                        .build());
    }
}

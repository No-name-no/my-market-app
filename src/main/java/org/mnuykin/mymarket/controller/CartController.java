package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/cart/items")
public class CartController {

    final private CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping
    public Mono<String> getCart(Model model){
        return Mono.zip(
                cartService.getItems().collectList(),
                cartService.getTotal()
        ).doOnNext(tuple -> {
            model.addAttribute("items", tuple.getT1());
            model.addAttribute("total", tuple.getT2());
        }).thenReturn("cart");
    }

    @PostMapping
    public Mono<String> postCart(@RequestParam Long id,
                           @RequestParam ItemAction action){
        return cartService.executeAction(id, action).thenReturn("redirect:/cart/items");
    }
}
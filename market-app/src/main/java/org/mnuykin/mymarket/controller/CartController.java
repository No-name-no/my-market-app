package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.service.CartService;
import org.mnuykin.mymarket.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Controller
@RequestMapping("/cart/items")
public class CartController {

    final private CartService cartService;
    final private PaymentService paymentService;

    @Autowired
    public CartController(CartService cartService, PaymentService paymentService){
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public Mono<String> getCart(Model model){
        return Mono.zip(
                cartService.getItems().collectList(),
                cartService.getTotal(),
                paymentService.getBalance().onErrorResume(throwable -> Mono.just(Long.MIN_VALUE))
        ).doOnNext(tuple -> {
            model.addAttribute("items", tuple.getT1());
            model.addAttribute("total", tuple.getT2());
            model.addAttribute("isPaymentServiceNotAvailable", tuple.getT3() == Long.MIN_VALUE);
            model.addAttribute("isUnsufficientFunds", tuple.getT3() != Long.MIN_VALUE
                    && tuple.getT3() < tuple.getT2());
        }).thenReturn("cart");
    }

    @PostMapping
    public Mono<String> postCart(ServerWebExchange exchange){
        return exchange.getFormData().flatMap(formData -> {
            final Long id = Long.valueOf(Objects.requireNonNull(formData.getFirst("id")));
            final ItemAction action = ItemAction.valueOf(formData.getFirst("action"));
            return cartService.executeAction(id, action).thenReturn("redirect:/cart/items");
        });
    }
}
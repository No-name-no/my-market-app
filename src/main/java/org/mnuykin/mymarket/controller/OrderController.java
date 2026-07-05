package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {
    private OrderService orderService;

    public OrderController (OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders (Model model){
        //TODO
        return "orders";
    }

    @GetMapping("/orders/{id}")
    String getOrder(@PathVariable Long id,
                    @RequestParam(defaultValue = "false") boolean newOrder,
                    Model model){
        //TODO
        return "order";
    }

    @PostMapping("/buy")
    String buy(Model model){
        //TODO
        return "redirect:/orders";
    }
}

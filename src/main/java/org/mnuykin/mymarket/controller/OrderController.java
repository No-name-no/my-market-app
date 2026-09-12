package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.model.OrderDto;
import org.mnuykin.mymarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {
    final private OrderService orderService;

    @Autowired
    public OrderController (OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders (Model model){
        model.addAttribute("orders", orderService.getOrder());
        return "orders";
    }

    @GetMapping("/orders/{id}")
    String getOrder(@PathVariable Long id,
                    @RequestParam(defaultValue = "false") boolean newOrder,
                    Model model){
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("newOrder", newOrder);
        return "order";
    }

    @PostMapping("/buy")
    String buy(RedirectAttributes attributes){
        OrderDto order = orderService.create();
        attributes.addAttribute("id", order.getId());
        attributes.addAttribute("newOrder", true);
        return "redirect:/orders/{id}" ;
    }
}

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart/items")
public class CartController {

    final private CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping
    public String getCart(Model model){
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        return "cart";
    }

    @PostMapping
    public String postCart(@RequestParam Long id,
                           @RequestParam ItemAction action,
                           RedirectAttributes attributes){
        cartService.executeAction(id, action);
        attributes.addAttribute("items", cartService.getItems());
        attributes.addAttribute("total", cartService.getTotal());
        return "redirect:/cart";
    }
}
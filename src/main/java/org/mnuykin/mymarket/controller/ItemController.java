package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.model.Item;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.model.Paging;
import org.mnuykin.mymarket.service.CartService;
import org.mnuykin.mymarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/", "/items"})
public class ItemController {

    final private ItemService itemService;
    final private CartService cartService;

    @Autowired
    public ItemController(ItemService itemService, CartService cartService){
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping
    public String getItems (@RequestParam (required = false) String search,
                                @RequestParam (defaultValue = ItemsSort.DEFAULT) ItemsSort sort,
                                @RequestParam (defaultValue =  "1") Integer pageNumber,
                                @RequestParam (defaultValue = "5") Integer pageSize,
                                Model model){
        Page<Item> itemPage = itemService.findItems(search, sort, pageNumber, pageSize);
        model.addAttribute("items", itemPage.getContent()/*TODO:ПЕРЕДЕЛАТЬ по три элемента*/);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", new Paging(
                itemPage.getSize(),
                itemPage.getNumber(),
                itemPage.hasPrevious(),
                itemPage.hasNext()
        ));

        return "items";
    }

    @PostMapping
    public String postItems (@RequestParam Long id,
                            @RequestParam String search,
                            @RequestParam (defaultValue = ItemsSort.DEFAULT) ItemsSort sort,
                            @RequestParam (defaultValue =  "1") Integer pageNumber,
                            @RequestParam (defaultValue = "5") Integer pageSize,
                            @RequestParam ItemAction action, RedirectAttributes attributes){
        cartService.executeAction(id, action);
        attributes.addAttribute("search", search);
        attributes.addAttribute("sort", sort);
        attributes.addAttribute("pageNumber", pageNumber);
        attributes.addAttribute("pageSize", pageSize);

        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String getItem (@PathVariable Long id,
                           Model model){
        Item item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return  "item";
    }

    @PostMapping("/{id}")
    public String getItem (@PathVariable Long id,
                           @RequestParam ItemAction action,
                           Model model){
        cartService.executeAction(id, action);
        model.addAttribute("item", itemService.getItemById(id));
        return  "item";
    }
}

package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/", "/items"})
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping
    public String getItems (@RequestParam (required = false) String search,
                                @RequestParam (defaultValue = ItemsSort.DEFAULT) ItemsSort itemsSort,
                                @RequestParam (defaultValue =  "1") Integer pageNumber,
                                @RequestParam (defaultValue = "5") Integer pageSize,
                                Model model){
        //TODO
        return "items";
    }

    @PostMapping
    public String postItems (@RequestParam Long id,
                            @RequestParam String search,
                            @RequestParam (defaultValue = ItemsSort.DEFAULT) ItemsSort itemsSort,
                            @RequestParam (defaultValue =  "1") Integer pageNumber,
                            @RequestParam (defaultValue = "5") Integer pageSize,
                            @RequestParam ItemAction itemAction,
                            Model model){
        //TODO
        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String getItem (@PathVariable Long id,
                           Model model){
        //TODO
        return  "item";
    }

    @PostMapping("/{id}")
    public String getItem (@PathVariable Long id,
                           @RequestParam ItemAction itemAction,
                           Model model){
        //TODO
        return  "item";
    }
}

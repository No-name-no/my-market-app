package org.mnuykin.mymarket.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.mnuykin.mymarket.model.ItemDto;
import org.mnuykin.mymarket.model.ItemAction;
import org.mnuykin.mymarket.model.ItemsSort;
import org.mnuykin.mymarket.model.PagingDto;
import org.mnuykin.mymarket.service.CartService;
import org.mnuykin.mymarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@Validated
public class ItemController {

    final private ItemService itemService;
    final private CartService cartService;

    @Autowired
    public ItemController(ItemService itemService, CartService cartService){
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping({"/", "/items"})
    public String getItems (@RequestParam (required = false) String search,
                                @RequestParam (defaultValue = ItemsSort.DEFAULT) ItemsSort sort,
                                @RequestParam (defaultValue = "1") @Min(1) @Max(Integer.MAX_VALUE) Integer pageNumber,
                                @RequestParam (defaultValue = "5") @Min(1) @Max(100) Integer pageSize,
                                Model model){
        Page<ItemDto> itemPage = itemService.findItems(search, sort, Math.max(0, pageNumber-1), pageSize);
        model.addAttribute("items", toAttributeItems(itemPage.getContent()));
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", new PagingDto(
                itemPage.getSize(),
                itemPage.getNumber(),
                itemPage.hasPrevious(),
                itemPage.hasNext()
        ));

        return "items";
    }

    @PostMapping({"/", "/items"})
    public String postItems (@RequestParam Long id,
                            @RequestParam String search,
                            @RequestParam (defaultValue = ItemsSort.DEFAULT) ItemsSort sort,
                            @RequestParam (defaultValue =  "1") @Min(1) @Max(Integer.MAX_VALUE) Integer pageNumber,
                            @RequestParam (defaultValue = "5") @Min(1) @Max(100) Integer pageSize,
                            @RequestParam ItemAction action, RedirectAttributes attributes){
        cartService.executeAction(id, action);
        attributes.addAttribute("search", search);
        attributes.addAttribute("sort", sort);
        attributes.addAttribute("pageNumber", pageNumber);
        attributes.addAttribute("pageSize", pageSize);

        return "redirect:/items";
    }

    @GetMapping("/items/{id}")
    public String getItem (@PathVariable Long id,
                           Model model){
        ItemDto item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return  "item";
    }

    @PostMapping("/items/{id}")
    public String getItem (@PathVariable Long id,
                           @RequestParam ItemAction action,
                           RedirectAttributes attributes){
        cartService.executeAction(id, action);
        attributes.addAttribute("item", itemService.getItemById(id));
        return  "redirect:item";
    }

    private List<List<ItemDto>> toAttributeItems (List<ItemDto> list) {
        final int attrSize = 3;
        final List<List<ItemDto>> attrList = new ArrayList<>();

        for (int i = 0; i < list.size(); i += attrSize) {
            final int toIndex = Math.min((i + attrSize), list.size());
            List<ItemDto> row = new ArrayList<>(list.subList(i, toIndex));

            while (row.size() < attrSize) {
                row.add(ItemDto.mockItem());
            }
            attrList.add(row);
        }

        return attrList;
    }
}
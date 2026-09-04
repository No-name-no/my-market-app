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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Mono<String> getItems (@RequestParam (required = false) String search,
                                 @RequestParam (defaultValue = ItemsSort.DEFAULT) ItemsSort sort,
                                 @RequestParam (defaultValue = "1") @Min(1) @Max(Integer.MAX_VALUE) Integer pageNumber,
                                 @RequestParam (defaultValue = "5") @Min(1) @Max(100) Integer pageSize,
                                 Model model){
        return itemService.findItems(search, sort, Math.max(0, pageNumber-1), pageSize)
                .doOnNext(
                        itemPage -> {
                            model.addAttribute("items", toAttributeItems(itemPage.getContent()));
                            model.addAttribute("search", search);
                            model.addAttribute("sort", sort);
                            model.addAttribute("paging", new PagingDto(
                                    itemPage.getSize(),
                                    itemPage.getNumber() + 1,
                                    itemPage.isHasPrevious(),
                                    itemPage.isHasNex()
                            ));
                        }
                ).thenReturn("items");
    }

    @PostMapping({"/", "/items"})
    public Mono<Rendering> postItems(ServerWebExchange exchange) {
        return exchange.getFormData()
                .flatMap(formData -> {
                    Long id = Long.parseLong(Objects.requireNonNull(formData.getFirst("id")));
                    String search = formData.getFirst("search");
                    ItemsSort sort = ItemsSort.valueOf(formData.getFirst("sort"));
                    int pageNumber = Integer.parseInt(Objects.requireNonNull(formData.getFirst("pageNumber")));
                    int pageSize = Integer.parseInt(Objects.requireNonNull(formData.getFirst("pageSize")));
                    ItemAction action = ItemAction.valueOf(formData.getFirst("action"));

                    return cartService.executeAction(id, action)
                            .then(Mono.just(
                                    Rendering.redirectTo(
                                        UriComponentsBuilder.fromPath("/items")
                                            .queryParam("search", search)
                                            .queryParam("pageNumber", pageNumber)
                                            .queryParam("sort", sort)
                                            .queryParam("pageSize", pageSize)
                                            .build().toUri().toString()
                                        ).build()
                                    )
                            );
                });
    }

    @GetMapping("/items/{id}")
    public Mono<Rendering> getItem(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(itemDto -> Rendering.view("item")
                        .modelAttribute("item", itemDto)
                        .build());
    }

    @PostMapping("/items/{id}")
    public Mono<Rendering> getItem2 (@PathVariable Long id,
                                    ServerWebExchange exchange){
        return exchange.getFormData().flatMap(formData -> {
            final ItemAction action = ItemAction.valueOf(formData.getFirst("action"));
            return cartService.executeAction(id, action)
                    .then(Mono.just(Rendering.redirectTo("/items/" + id).build()));
        });
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
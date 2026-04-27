package com.bawantha.microSpring.controller;

import com.bawantha.microSpring.entity.Item;
import com.bawantha.microSpring.service.CartService;
import com.bawantha.microSpring.service.ItemService;
import reactor.core.publisher.Mono;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.result.view.Rendering;

@Controller
public class HomeController {

    private final CartService inventoryService;
    private final ItemService itemService;

    public HomeController(CartService inventoryService, ItemService itemService) {
        this.inventoryService = inventoryService;
        this.itemService = itemService;
    }

    // Task 11: Handle the OAuth Authentication in the Controller
    @GetMapping("/")
    Mono<Rendering> home(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            // Not logged in — show public view with inventory only
            return Mono.just(Rendering.view("index")
                    .modelAttribute("inventory", this.inventoryService.getInventory())
                    .build());
        }
        // Logged in — show personalised cart
        return Mono.just(Rendering.view("index")
                .modelAttribute("cart", this.inventoryService.getCart(cartName(oAuth2User)))
                .modelAttribute("inventory", this.inventoryService.getInventory())
                .modelAttribute("cartName", cartName(oAuth2User))
                .build());
    }

    // Task 12: Add POST and DELETE Map
    @PostMapping("/add/{id}")
    @ResponseBody
    Mono<Void> addToCart(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @PathVariable String id) {
        return this.inventoryService.addItemToCart(cartName(oAuth2User), id).then();
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    Mono<Void> removeFromCart(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @PathVariable String id) {
        return this.inventoryService.removeOneFromCart(cartName(oAuth2User), id).then();
    }

    @PostMapping("/item")
    @ResponseBody
    Mono<Item> createItem(@RequestBody Item newItem) {
        return this.itemService.saveItem(newItem);
    }

    @DeleteMapping("/item/{id}")
    @ResponseBody
    Mono<Void> deleteItem(@PathVariable String id) {
        return this.itemService.deleteItem(id);
    }

    private static String cartName(OAuth2User oAuth2User) {
        return oAuth2User.getName() + "'s Cart";
    }

}

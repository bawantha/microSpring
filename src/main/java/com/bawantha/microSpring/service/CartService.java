package com.bawantha.microSpring.service;

import com.bawantha.microSpring.entity.Cart;
import com.bawantha.microSpring.entity.Item;
import com.bawantha.microSpring.repository.CartRepository;
import com.bawantha.microSpring.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public CartService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public Flux<Item> getInventory() {
        return itemRepository.findAll();
    }

    public Flux<Cart> getCart() {
        return cartRepository.findAll();
    }

    public Mono<Item> saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Mono<Void> deleteItem(String id) {
        return itemRepository.deleteById(id);
    }

}

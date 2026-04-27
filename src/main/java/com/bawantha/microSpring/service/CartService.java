package com.bawantha.microSpring.service;

import com.bawantha.microSpring.entity.Cart;
import com.bawantha.microSpring.entity.CartItem;
import com.bawantha.microSpring.entity.Item;
import com.bawantha.microSpring.repository.CartRepository;
import com.bawantha.microSpring.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

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

    public Mono<Cart> getCart(String cartId) {
        return cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId, new ArrayList<>()));
    }

    public Mono<Item> saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Mono<Void> deleteItem(String id) {
        return itemRepository.deleteById(id);
    }

    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        // Find cart by cartId, or create a new empty cart if not found
        return cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId, new ArrayList<>()))
                .flatMap(cart -> {
                    // Stream through existing cart items to find one matching itemId
                    Optional<CartItem> existingItem = cart.getItems().stream()
                            .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                            .findFirst();

                    if (existingItem.isPresent()) {
                        // Item found — increment quantity and return cart
                        existingItem.get().increment();
                        return Mono.just(cart);
                    } else {
                        // Item not found — retrieve from repo, create CartItem, add to cart
                        return itemRepository.findById(itemId)
                                .map(item -> {
                                    CartItem cartItem = new CartItem();
                                    cartItem.setItem(item);
                                    cartItem.increment();
                                    cart.getItems().add(cartItem);
                                    return cart;
                                });
                    }
                })
                // Save the modified cart back to the repository
                .flatMap(cartRepository::save);
    }

    public Mono<Cart> removeOneFromCart(String cartId, String itemId) {
        // Find cart by cartId, or create a new empty cart if not found
        return cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId, new ArrayList<>()))
                .flatMap(cart -> {
                    // Find the cart item matching itemId and decrement its quantity
                    cart.getItems().stream()
                            .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                            .findFirst()
                            .ifPresent(CartItem::decrement);

                    return Mono.just(cart);
                })
                .map(cart -> {
                    // Rebuild cart excluding items with quantity <= 0
                    java.util.List<CartItem> updatedItems = cart.getItems().stream()
                            .filter(cartItem -> cartItem.getQuantity() > 0)
                            .collect(java.util.stream.Collectors.toList());

                    return new Cart(cart.getId(), updatedItems);
                })
                // Save the updated cart back to the repository
                .flatMap(cartRepository::save);
    }

}

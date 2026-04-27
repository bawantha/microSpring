package com.bawantha.microSpring.service;

import com.bawantha.microSpring.entity.Item;
import com.bawantha.microSpring.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Mono<Item> saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Mono<Void> deleteItem(String id) {
        return itemRepository.deleteById(id);
    }

}

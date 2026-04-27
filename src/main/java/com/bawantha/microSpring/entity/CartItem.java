package com.bawantha.microSpring.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "cart_items")
public class CartItem {

    private Item item;
    private int quantity;

    public void increment() {
        this.quantity++;
    }

    public void decrement() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

}

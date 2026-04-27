package com.bawantha.microSpring.repository;

import com.bawantha.microSpring.entity.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}

package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.Cart;
import com.example.hralgebrajavawebshop.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepositoryInterface extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart(Cart cart);
}

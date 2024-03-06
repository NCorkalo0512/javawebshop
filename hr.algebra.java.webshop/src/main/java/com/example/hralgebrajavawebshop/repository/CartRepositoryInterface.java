package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.Cart;
import com.example.hralgebrajavawebshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepositoryInterface extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(User user);
    List<Cart> findByUserOrderByCreatedAtDesc(User user);

}

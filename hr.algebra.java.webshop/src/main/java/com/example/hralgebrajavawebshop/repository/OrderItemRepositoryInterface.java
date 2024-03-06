package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.Order;
import com.example.hralgebrajavawebshop.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepositoryInterface extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder(Order order);
}

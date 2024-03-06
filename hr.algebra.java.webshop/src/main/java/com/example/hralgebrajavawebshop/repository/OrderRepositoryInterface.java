package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.Order;
import com.example.hralgebrajavawebshop.models.OrderItem;
import com.example.hralgebrajavawebshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryInterface extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    List<Order> findByUserAndOrderTimeBetween(Optional<User> user, Date start, Date end);


    List<Order> findByOrderTimeBetween(Date start, Date end);
    List<Order> findByUserUsernameOrderByOrderTimeDesc(String username);
}

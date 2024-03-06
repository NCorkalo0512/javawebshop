package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.dto.OrderDTO;
import com.example.hralgebrajavawebshop.models.Order;
import com.example.hralgebrajavawebshop.models.OrderItem;
import com.example.hralgebrajavawebshop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    private final OrderRepositoryInterface orderRepositoryInterface;
    private final UserRepositoryInterface userRepositoryInterface;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderRepository(OrderRepositoryInterface orderRepositoryInterface, UserRepositoryInterface userRepositoryInterface, OrderItemRepository orderItemRepository) {
        this.orderRepositoryInterface = orderRepositoryInterface;
        this.userRepositoryInterface = userRepositoryInterface;
        this.orderItemRepository = orderItemRepository;
    }

   public List<Order>findByUser(User user){
        return orderRepositoryInterface.findByUser(user);
    }

   public List<Order>findAllOrders(){
        return orderRepositoryInterface.findAll();
   }

   public Optional<Order>findOrderById(Integer id){
        return orderRepositoryInterface.findById(id);
   }
    public List<Order> getOrdersByUserAndDateRange(Optional<User> user, Date start, Date end) {
        if (user.isPresent()) {
            return orderRepositoryInterface.findByUserAndOrderTimeBetween(Optional.of(user.get()), start, end);
        } else {
            return getOrdersByDateRange(start, end);
        }
    }
    public Order addOrder(OrderDTO orderDTO) {
        User user = userRepositoryInterface.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = new Order();
        order.setUser(user);
        order.setOrderTime(new Date());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        return orderRepositoryInterface.save(order);
    }

    public Order updateOrder(int orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepositoryInterface.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existingOrder.setTotalAmount(orderDTO.getTotalAmount());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());

        return orderRepositoryInterface.save(existingOrder);
    }

    public void deleteOrder(Integer id) {
        orderRepositoryInterface.deleteById(id);
    }


    public List<Order> getOrdersByDateRange(Date start, Date end) {
        return orderRepositoryInterface.findByOrderTimeBetween(start, end);
    }

    public List<Order> getOrderHistoryByUsername(String username) {
        return orderRepositoryInterface.findByUserUsernameOrderByOrderTimeDesc(username);
    }
}

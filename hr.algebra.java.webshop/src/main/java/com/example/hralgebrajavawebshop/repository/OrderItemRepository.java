package com.example.hralgebrajavawebshop.repository;


import com.example.hralgebrajavawebshop.dto.OrderItemDTO;
import com.example.hralgebrajavawebshop.models.Order;
import com.example.hralgebrajavawebshop.models.OrderItem;
import com.example.hralgebrajavawebshop.models.ProductGrocery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderItemRepository {
    private final OrderItemRepositoryInterface orderItemRepositoryInterface;
    private final ProductRepositoryInterface productRepositoryInterface;

    @Autowired
    public OrderItemRepository(OrderItemRepositoryInterface orderItemRepositoryInterface, ProductRepositoryInterface productRepositoryInterface) {
        this.orderItemRepositoryInterface = orderItemRepositoryInterface;
        this.productRepositoryInterface = productRepositoryInterface;
    }


    public List<OrderItem> findAllOrderItems() {
        return orderItemRepositoryInterface.findAll();
    }

    public Optional<OrderItem> findOrderItemById(Integer id) {
        return orderItemRepositoryInterface.findById(id);
    }

    public List<OrderItem> findOrderItemsByOrder(Order order) {
        return orderItemRepositoryInterface.findByOrder(order);
    }

    public OrderItem addOrderItem(OrderItemDTO orderItemDTO, Order order) {
        ProductGrocery product = productRepositoryInterface.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setPricePerItem(orderItemDTO.getPricePerItem());
        orderItem.setOrder(order);

        return orderItemRepositoryInterface.save(orderItem);
    }

    public OrderItem updateOrderItem(int orderItemId, OrderItemDTO orderItemDTO) {
        OrderItem existingOrderItem = orderItemRepositoryInterface.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        ProductGrocery product = productRepositoryInterface.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingOrderItem.setProduct(product);
        existingOrderItem.setQuantity(orderItemDTO.getQuantity());
        existingOrderItem.setPricePerItem(orderItemDTO.getPricePerItem());

        return orderItemRepositoryInterface.save(existingOrderItem);
    }

    public void deleteOrderItem(Integer id) {
        orderItemRepositoryInterface.deleteById(id);
    }
}

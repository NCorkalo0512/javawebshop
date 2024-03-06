package com.example.hralgebrajavawebshop.controller;

import com.example.hralgebrajavawebshop.dto.OrderItemDTO;
import com.example.hralgebrajavawebshop.models.Order;
import com.example.hralgebrajavawebshop.models.OrderItem;
import com.example.hralgebrajavawebshop.repository.OrderItemRepository;
import com.example.hralgebrajavawebshop.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/order-items")
@AllArgsConstructor
public class OrderItemController {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;


    @GetMapping
    public String listOrderItems(Model model) {
        List<OrderItem> orderItems = orderItemRepository.findAllOrderItems();
        model.addAttribute("orderItems", orderItems);
        return "order-items/list";
    }

    @GetMapping("/{id}")
    public String viewOrderItem(@PathVariable Integer id, Model model) {
        OrderItem orderItem = orderItemRepository.findOrderItemById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        model.addAttribute("orderItem", orderItem);
        return "order-items/view";
    }

    @GetMapping("/new/{orderItemId}")
    public String showNewOrderItemForm(@PathVariable Integer orderItemId, Model model) {
        model.addAttribute("orderItemDTO", new OrderItemDTO());
        model.addAttribute("orderId", orderItemId);
        return "order-items/new";
    }

    @PostMapping("/new/{orderId}")
    public String addOrderItem(@PathVariable Integer orderId, @ModelAttribute("orderItemDTO") @Valid OrderItemDTO orderItemDTO, BindingResult result) {
        if (result.hasErrors()) {

            return "/orderItems";
        }
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderItemRepository.addOrderItem(orderItemDTO, order);
        return "redirect:/order-items";
    }

    @GetMapping("/edit/{orderItemId}")
    public String showEditOrderItemForm(@PathVariable Integer orderItemId, Model model) {
        OrderItem orderItem = orderItemRepository.findOrderItemById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        model.addAttribute("orderItemDTO", orderItem.OrderItemtoDTO());
        model.addAttribute("orderItemId", orderItemId);
        return "order-items/edit";
    }

    @PostMapping("/update/{orderItemId}")
    public String updateOrderItem(@PathVariable Integer orderItemId, @ModelAttribute("orderItemDTO") OrderItemDTO orderItemDTO) {
        orderItemRepository.updateOrderItem(orderItemId, orderItemDTO);
        return "redirect:/order-items";
    }

    @GetMapping("/delete/{orderItemId}")
    public String deleteOrderItem(@PathVariable Integer orderItemId) {
        orderItemRepository.deleteOrderItem(orderItemId);
        return "redirect:/order-items";
    }
}

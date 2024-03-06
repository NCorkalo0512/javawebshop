package com.example.hralgebrajavawebshop.controller;

import com.example.hralgebrajavawebshop.dto.OrderDTO;
import com.example.hralgebrajavawebshop.models.Order;
import com.example.hralgebrajavawebshop.models.User;
import com.example.hralgebrajavawebshop.repository.OrderRepository;
import com.example.hralgebrajavawebshop.repository.UserRepositoryInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final UserRepositoryInterface userRepositoryInterface;


    @GetMapping
    public String listOrders(Model model){
        List<Order>orders= orderRepository.findAllOrders();
        model.addAttribute("orders",orders);
        return "listOrders";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Integer id, Model model) {
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "orders/view";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/user/{userId}")
    public String listOrdersByUser(@PathVariable Integer userId, Model model) {
        User user = userRepositoryInterface.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "orders/listOrders";
    }

    @GetMapping("/new")
    public String showNewOrderForm(Model model) {
        model.addAttribute("orderDTO", new OrderDTO());
        return "orders/newOrder";
    }

    @PostMapping
    public String addOrder(@ModelAttribute("orderDTO") @Valid OrderDTO orderDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "orders/newOrder";
        }
        orderRepository.addOrder(orderDTO);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable Integer id, Model model) {
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("orderDTO", order.OrderToDTO());
        return "orders/editOrder";
    }

    @PostMapping("/update/{id}")
    public String updateOrder(@PathVariable Integer id, @ModelAttribute("orderDTO") @Valid OrderDTO orderDTO,BindingResult result ){
        if (result.hasErrors()) {
            return "orders/editOrder";
        }
        orderRepository.updateOrder(id, orderDTO);
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderRepository.deleteOrder(id);
        return "redirect:/orders";
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/search")
    public String searchOrders(@RequestParam(required = false) Integer userId,
                               @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date start,
                               @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date end,
                               Model model) {
        List<Order> orders;
        if (userId != null) {
            Optional<User> user = userRepositoryInterface.findById(userId);
            if (user.isEmpty()) {
                model.addAttribute("error", "User not found");
                return "errorPage";
            }
            orders = orderRepository.getOrdersByUserAndDateRange(user, start, end);
        } else {
            orders = orderRepository.getOrdersByUserAndDateRange(Optional.empty(), start, end);
        }
        model.addAttribute("orders", orders);
        return "listOrders";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/orderConfirmation/{orderId}")
    public String showOrderConfirmation(@PathVariable Integer orderId, Model model) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "orderConfirmation";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/orderHistory")
    public String orderHistory(Model model, Principal principal) {
        String username = principal.getName();
        List<Order> orderHistory = orderRepository.getOrderHistoryByUsername(username);

        model.addAttribute("orderHistory", orderHistory);
        return "orderHistory";
    }

}

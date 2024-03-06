package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.dto.CartDTO;
import com.example.hralgebrajavawebshop.dto.CartItemDTO;
import com.example.hralgebrajavawebshop.dto.OrderDTO;
import com.example.hralgebrajavawebshop.dto.OrderItemDTO;
import com.example.hralgebrajavawebshop.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CartRepository {
    private final CartRepositoryInterface cartRepositoryInterface;
    private final CartItemRepositoryInterface cartItemRepositoryInterface;
    private final UserRepositoryInterface userRepositoryInterface;

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartRepository(CartRepositoryInterface cartRepositoryInterface, CartItemRepositoryInterface cartItemRepositoryInterface,
                          UserRepositoryInterface userRepositoryInterface, ProductRepository productRepository, CartItemRepository cartItemRepository,
                          OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.cartRepositoryInterface = cartRepositoryInterface;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepositoryInterface = userRepositoryInterface;
        this.cartItemRepositoryInterface = cartItemRepositoryInterface;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Cart> findAllCarts() {
        return cartRepositoryInterface.findAll();
    }

    public Optional<Cart> findCartById(Integer id) {
        return cartRepositoryInterface.findById(id);
    }

    public List<Cart> findCartsByUser(User user) {
        return cartRepositoryInterface.findByUser(user);
    }

    public Cart findLatestCartByUser(User user) {
        List<Cart> carts = cartRepositoryInterface.findByUserOrderByCreatedAtDesc(user);
        if (carts.isEmpty()) {
            return null;
        }
        return carts.get(0);
    }

    public Cart addCart(CartDTO cartDTO) {
        User user = userRepositoryInterface.findById(cartDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(new Date());
        Cart savedCart = cartRepositoryInterface.save(cart);

        if(cartDTO.getItems() != null){
            for (CartItemDTO itemDTO : cartDTO.getItems()) {
                cartItemRepository.addCartItem(itemDTO, savedCart);
            }
        }

        return savedCart;
    }

    public Cart updateCart(Integer cartId, CartDTO cartDTO) {
        Cart cart = cartRepositoryInterface.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        for (CartItemDTO itemDTO : cartDTO.getItems()) {
            Integer cartItemId = itemDTO.getProductId();
            cartItemRepository.updateCartItem(cartItemId, itemDTO);
        }
        return cart;
    }

    public void deleteCart(Integer id) {
        cartRepositoryInterface.deleteById(id);
    }


    public Order checkout(Integer cartId) {
        Cart cart = cartRepositoryInterface.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        double totalAmount = calculateTotalAmount(cart);
        processPayment(totalAmount);
        var order = createOrderRecord(cart, totalAmount);
        clearCart(cart);
        return order;
    }

    public double calculateTotalAmount(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            total = total.add(price.multiply(quantity));
        }

        return total.doubleValue();
    }

    private void processPayment(double amount) {
        // Implementacija za proces plaćanja- PAYPAL CE BITI
    }

    private Order createOrderRecord(Cart cart, double totalAmount) {
        var orderDto = new OrderDTO();
        orderDto.setTotalAmount(BigDecimal.valueOf(totalAmount));

        orderDto.setUserId(cart.getUser().getIdUsers());
        orderDto.setPaymentMethod("PAYPAL");
        var orderItems = new ArrayList<OrderItemDTO>();
        for(var cartItem : cart.getItems()){
            var orderItemDto = new OrderItemDTO();
            orderItemDto.setPricePerItem(cartItem.getProduct().getPrice());
            orderItemDto.setQuantity(cartItem.getQuantity());
            orderItemDto.setProductId(cartItem.getProduct().getIdProduct());
            orderItems.add(orderItemDto);
        }
        orderDto.setItems(orderItems);
        var order = orderRepository.addOrder(orderDto);
        for(var orderItem : orderItems){
            orderItemRepository.addOrderItem(orderItem, order);
        }
        return order;
    }

    private void clearCart(Cart cart) {
        cart.getItems().clear();
        cart.setItems(null);
        cartRepositoryInterface.save(cart);
    }

}

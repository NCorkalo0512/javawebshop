package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.dto.CartItemDTO;
import com.example.hralgebrajavawebshop.models.Cart;
import com.example.hralgebrajavawebshop.models.CartItem;
import com.example.hralgebrajavawebshop.models.ProductGrocery;
import com.example.hralgebrajavawebshop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartItemRepository {
    private final CartItemRepositoryInterface cartItemRepositoryInterface;
    private ProductRepositoryInterface productRepositoryInterface;
    @Autowired
    public CartItemRepository(CartItemRepositoryInterface cartItemRepositoryInterface,ProductRepositoryInterface productRepositoryInterface) {
        this.cartItemRepositoryInterface = cartItemRepositoryInterface;
        this.productRepositoryInterface=productRepositoryInterface;
    }


    public List<CartItem> findAllCartItems() {
        return cartItemRepositoryInterface.findAll();
    }

    public Optional<CartItem> findCartItemById(Integer id) {
        return cartItemRepositoryInterface.findById(id);
    }

    public List<CartItem> findCartItemsByCart(Cart cart) {
        return cartItemRepositoryInterface.findByCart(cart);
    }


    public CartItem addCartItem(CartItemDTO cartItemDTO, Cart cart) {
        ProductGrocery product = productRepositoryInterface.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setCart(cart);
        return cartItemRepositoryInterface.save(cartItem);
    }

    public CartItem updateCartItem(Integer cartItemId, CartItemDTO cartItemDTO) {
        CartItem existingCartItem = cartItemRepositoryInterface.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        ProductGrocery product = productRepositoryInterface.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingCartItem.setProduct(product);
        existingCartItem.setQuantity(cartItemDTO.getQuantity());

        return cartItemRepositoryInterface.save(existingCartItem);
    }

    public void deleteCartItem(Integer id) {
        cartItemRepositoryInterface.deleteById(id);
    }
}

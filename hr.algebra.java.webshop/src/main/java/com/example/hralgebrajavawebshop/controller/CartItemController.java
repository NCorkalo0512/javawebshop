package com.example.hralgebrajavawebshop.controller;

import com.example.hralgebrajavawebshop.dto.CartItemDTO;
import com.example.hralgebrajavawebshop.models.Cart;
import com.example.hralgebrajavawebshop.models.CartItem;
import com.example.hralgebrajavawebshop.repository.CartItemRepository;
import com.example.hralgebrajavawebshop.repository.CartRepository;
import com.example.hralgebrajavawebshop.repository.UserRepository;
import com.example.hralgebrajavawebshop.repository.UserRepositoryInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/cart-items")
@AllArgsConstructor
public class CartItemController {
    private final UserRepositoryInterface userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserRepositoryInterface userRepositoryInterface;

    @GetMapping
    public String listCartItems(Model model) {
        List<CartItem> cartItems = cartItemRepository.findAllCartItems();
        model.addAttribute("cartItems", cartItems);
        return "cart-items/list";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/{id}")
    public String viewCartItem(@PathVariable Integer id, Model model) {
        CartItem cartItem = cartItemRepository.findCartItemById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        model.addAttribute("cartItem", cartItem);
        return "cart-items/view";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/new/{cartId}")
    public String showNewCartItemForm(@PathVariable Integer cartId, Model model) {
        model.addAttribute("cartItemDTO", new CartItemDTO());
        model.addAttribute("cartId", cartId);
        return "cart-items/new";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/add")
    public String addCartItem(@RequestBody CartItemDTO cartItemDTO, BindingResult result, @AuthenticationPrincipal UserDetails currentUser) {
        String username = currentUser.getUsername();
        var user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findLatestCartByUser(user);

        cartItemRepository.addCartItem(cartItemDTO, cart);
        return "/api/listProducts";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/edit/{id}")
    public String showEditCartItemForm(@PathVariable Integer id, Model model) {
        CartItem cartItem = cartItemRepository.findCartItemById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        model.addAttribute("cartItemDTO", cartItem.CartItemToDTO());
        model.addAttribute("cartItemId", id);
        return "cart-items/edit";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/update/{id}")
    public String updateCartItem(@PathVariable Integer id, @ModelAttribute("cartItemDTO") @Valid CartItemDTO cartItemDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "cartItems/edit";
        }

        cartItemRepository.updateCartItem(id, cartItemDTO);
        return "redirect:/cart-items";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable Integer id,Model model, @AuthenticationPrincipal UserDetails currentUser) {
        cartItemRepository.deleteCartItem(id);
        String username = currentUser.getUsername();
        var user = userRepositoryInterface.findByUsername(username);
        Cart cart = cartRepository.findLatestCartByUser(user);

        var cartItems = cart.getItems();
        model.addAttribute("items", cartItems);
        return "/api/carts/latest";
    }
}

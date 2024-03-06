package com.example.hralgebrajavawebshop.controller;

import com.example.hralgebrajavawebshop.dto.CartDTO;
import com.example.hralgebrajavawebshop.models.Cart;
import com.example.hralgebrajavawebshop.models.User;
import com.example.hralgebrajavawebshop.repository.CartRepository;
import com.example.hralgebrajavawebshop.repository.UserRepositoryInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;


@Controller
@RequestMapping("/api/carts")
@AllArgsConstructor
@SessionAttributes("cart")
public class CartController {
    private final CartRepository cartRepository;
    private final UserRepositoryInterface userRepositoryInterface;
    private final PayPalService payPalService;
    @ModelAttribute("cart")
    public Cart initializeCart() {
        return new Cart();
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    public String listCarts(Model model){
        List<Cart>carts= cartRepository.findAllCarts();
        model.addAttribute("carts",carts);
        return "newcarts";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/latest")
    public String viewCart(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        String username = currentUser.getUsername();
        var user = userRepositoryInterface.findByUsername(username);
        Cart cart = cartRepository.findLatestCartByUser(user);


        var totalPrice = cartRepository.calculateTotalAmount(cart);
        var cartItems = cart.getItems();
        model.addAttribute("items", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "newcarts";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/new")
    public String addCart(@AuthenticationPrincipal UserDetails currentUser) {
        String username = currentUser.getUsername();
        var user = userRepositoryInterface.findByUsername(username);
        var cartDTO = new CartDTO();
        cartDTO.setUserId(user.getIdUsers());

        cartRepository.addCart(cartDTO);
        return "newcarts";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/edit/{id}")
    public String showEditCartForm(@PathVariable Integer id, Model model) {
        Cart cart = cartRepository.findCartById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        model.addAttribute("cartDTO", cart.CartToDTO());
        return "redirect:/carts";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/update/{id}")
    public String updateCart(@PathVariable Integer id, @ModelAttribute("cartDTO")  @Valid CartDTO cartDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "newcarts";
        }
        cartRepository.updateCart(id, cartDTO);
        return "redirect:/carts";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/delete/{id}")
    public String deleteCart(@PathVariable Integer id, SessionStatus sessionStatus) {
        cartRepository.deleteCart(id);
        sessionStatus.setComplete();
        return "redirect:/listProducts";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/checkout")
    public String checkout(@RequestParam(required = false) String paymentMethod, Model model, SessionStatus sessionStatus, @AuthenticationPrincipal UserDetails currentUser) {
        String username = currentUser.getUsername();
        var user = userRepositoryInterface.findByUsername(username);
        Cart cart = cartRepository.findLatestCartByUser(user);

        if ("paypal".equals(paymentMethod)) {
            try {
                String redirectUrl = payPalService.createPayment(cart);
                return "redirect:" + redirectUrl;
            } catch (Exception e) {
                model.addAttribute("error", "Error during PayPal payment creation: " + e.getMessage());
                return "newcarts";
            }
        }

        var order = cartRepository.checkout(cart.getIdCart());
        model.addAttribute("order", order);
        sessionStatus.setComplete();
        return "orderConfirmation";
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/checkout/paypal")
    public String handlePayPalCheckout( Model model, @AuthenticationPrincipal UserDetails currentUser) {
        try {
            String username = currentUser.getUsername();
            var user = userRepositoryInterface.findByUsername(username);
            Cart cart = cartRepository.findLatestCartByUser(user);
            String redirectUrl = payPalService.createPayment(cart);
            return "redirect:" + redirectUrl;
        } catch (Exception e) {
            model.addAttribute("error", "Error during PayPal payment creation: " + e.getMessage());
            return "newcarts";
        }
    }
}

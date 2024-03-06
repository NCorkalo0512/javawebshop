package com.example.hralgebrajavawebshop.controller;

import com.example.hralgebrajavawebshop.dto.ProductGroceryDTO;
import com.example.hralgebrajavawebshop.models.CategoryProduct;
import com.example.hralgebrajavawebshop.models.ProductGrocery;
import com.example.hralgebrajavawebshop.models.User;
import com.example.hralgebrajavawebshop.repository.CategoryRepositoryInterface;
import com.example.hralgebrajavawebshop.repository.ProductRepository;
import com.example.hralgebrajavawebshop.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private UserRepository userRepository;
    private final CategoryRepositoryInterface categoryRepositoryInterface;

    public int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findUserByUsername(userDetails.getUsername());
            return user.getIdUsers();
        }
        return 0;
    }

    @GetMapping("/api/listProducts")
    public String listProducts(Model model, @AuthenticationPrincipal UserDetails userDetails){
        List<ProductGrocery>productGroceries= productRepository.findAllProducts();
        model.addAttribute("products", productGroceries);
        int userId = getCurrentUserId();
        model.addAttribute("userId", userId);
        return "listProducts";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/api/products/newProduct")
    public String showNewProductForm(Model model){
        model.addAttribute("productDTO", new ProductGroceryDTO());
        return "newProduct";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/api/products")
    public String addProduct(@ModelAttribute("productDTO") @Valid ProductGroceryDTO productGroceryDTO, BindingResult result,Model model){
        if (result.hasErrors()) {
            model.addAttribute("productDTO", productGroceryDTO);
            return "newProduct";
        }
        productRepository.saveProduct(productGroceryDTO);
        return "listProducts";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/api/products/editProduct/{id}")
    public String showEditProductForm(@PathVariable Integer id, Model model) {
        ProductGrocery product = productRepository.findProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("productDTO", product.toDTO());
        return "editProduct";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/api/products/update/{id}")
    public String updateProduct(@PathVariable Integer id, @ModelAttribute("productDTO") @Valid ProductGroceryDTO productDTO, BindingResult result,Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productDTO", productDTO);
            return "/editProduct";
        }
        productRepository.updateProduct(id, productDTO);
        return "redirect:/api/listProducts";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/api/products/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productRepository.deleteProduct(id);
        return "redirect:/api/listProducts";
    }

    /*KUPCI*/
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/api/products/search/by-name")
    public String searchProductsByName(@RequestParam("name") String name, Model model) {
        List<ProductGrocery> products = productRepository.findProductsByName(name);
        model.addAttribute("products", products);
        return "listProducts";
    }


    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/api/products/search/by-category-name")
    public ResponseEntity<List<ProductGrocery>> searchProductsByCategory(@RequestParam("name") String categoryName) {
        List<CategoryProduct> categories = categoryRepositoryInterface.findCategoryByNameCategory(categoryName);

        List<ProductGrocery> products = new ArrayList<ProductGrocery>();
        for(CategoryProduct categoryEntry : categories){
            products.addAll(productRepository.findProductsByCategory(categoryEntry));
        }

        return ResponseEntity.ok(products);
    }
}

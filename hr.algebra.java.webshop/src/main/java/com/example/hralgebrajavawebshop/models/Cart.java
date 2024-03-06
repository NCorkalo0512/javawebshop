package com.example.hralgebrajavawebshop.models;


import com.example.hralgebrajavawebshop.dto.CartDTO;
import com.example.hralgebrajavawebshop.dto.ProductGroceryDTO;
import com.paypal.api.payments.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCart;

    @ManyToOne
    @JoinColumn(name = "UsersID", referencedColumnName = "idUsers")
    private User user;

    @Column(name = "CreatedAt")
    private Date createdAt;
    @OneToMany(mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();


    public CartDTO CartToDTO() {
        CartDTO dto = new CartDTO();
        dto.setItems(this.CartToDTO().getItems());
        if (this.getUser() != null) {
            dto.setUserId(this.getUser().getIdUsers());
        }
        return dto;
    }

    public void addProduct(ProductGrocery product, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setCart(this);
        items.add(cartItem);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idCart=" + idCart +
                ", user=" + user +
                ", items=" + items.stream().map(item -> item.getIdCartItem()) +
                '}';
    }

}

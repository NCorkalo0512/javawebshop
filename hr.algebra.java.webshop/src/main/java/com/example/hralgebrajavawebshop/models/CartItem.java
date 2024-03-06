package com.example.hralgebrajavawebshop.models;


import com.example.hralgebrajavawebshop.dto.CartItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CartItems")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCartItem;

    @Column(name = "Quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ProductID", referencedColumnName = "idProduct")
    private ProductGrocery product;

    @ManyToOne
    @JoinColumn(name = "CartID", referencedColumnName = "idCart")
    private Cart cart;

    public CartItemDTO CartItemToDTO() {
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(this.product != null ? this.product.getIdProduct() : null);
        dto.setQuantity(this.quantity);
        return dto;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "idCartItem=" + idCartItem +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}

package com.example.hralgebrajavawebshop.models;


import com.example.hralgebrajavawebshop.dto.OrderItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OrderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrderItem;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "PricePerItem")
    private BigDecimal pricePerItem;

    @ManyToOne
    @JoinColumn(name = "OrderID", referencedColumnName = "idOrder")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ProductID", referencedColumnName = "idProduct")
    private ProductGrocery product;


    public OrderItemDTO OrderItemtoDTO() {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(this.product != null ? this.product.getIdProduct() : null);
        dto.setQuantity(this.quantity);
        dto.setPricePerItem(this.pricePerItem);
        return dto;
    }
}

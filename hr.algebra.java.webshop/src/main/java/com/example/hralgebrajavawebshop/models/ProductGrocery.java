package com.example.hralgebrajavawebshop.models;


import com.example.hralgebrajavawebshop.dto.ProductGroceryDTO;
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
@Table(name = "ProductGrocery")
public class ProductGrocery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProduct;

    @Column(name = "NameProduct")
    private String nameProduct;

    @Column(name = "Description")
    private String description;

    @Column(name = "Price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "CategoryID", referencedColumnName = "idCategory")
    private CategoryProduct category;


    public ProductGroceryDTO toDTO() {
        ProductGroceryDTO dto = new ProductGroceryDTO();
        dto.setIdProduct(this.getIdProduct());
        dto.setNameProduct(this.getNameProduct());
        dto.setDescription(this.getDescription());
        dto.setPrice(this.getPrice());
        if (this.getCategory() != null) {
            dto.setCategoryId(this.getCategory().getIdCategory());
        }
        return dto;
    }
}

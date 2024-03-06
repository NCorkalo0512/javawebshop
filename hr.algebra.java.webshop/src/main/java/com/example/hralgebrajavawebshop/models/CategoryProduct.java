package com.example.hralgebrajavawebshop.models;


import com.example.hralgebrajavawebshop.dto.CategoryProductDTO;
import com.example.hralgebrajavawebshop.dto.ProductGroceryDTO;
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
@Table(name = "CategoryProduct")
public class CategoryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategory;

    @Column(name = "NameCategory")
    private String nameCategory;


}

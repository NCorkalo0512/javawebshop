package com.example.hralgebrajavawebshop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGroceryDTO {

    private int idProduct;

    @NotBlank(message= "Product name cannot be empty")
    private String nameProduct;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;


}

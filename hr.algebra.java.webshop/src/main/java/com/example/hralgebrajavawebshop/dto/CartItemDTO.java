package com.example.hralgebrajavawebshop.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    @NotNull(message = "Product ID must not be null")
    private Integer productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}

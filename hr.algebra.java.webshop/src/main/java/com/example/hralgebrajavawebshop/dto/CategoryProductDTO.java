package com.example.hralgebrajavawebshop.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProductDTO {

    @NotBlank(message= "Category name cannot be empty")
    private String nameCategory;

    private int idCategory;
}

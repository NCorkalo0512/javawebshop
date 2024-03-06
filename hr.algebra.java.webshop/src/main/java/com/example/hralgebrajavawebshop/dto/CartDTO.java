package com.example.hralgebrajavawebshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    @NotNull(message = "User ID must not be null")
    private Integer userId;

    private List< @Valid CartItemDTO> items;
}

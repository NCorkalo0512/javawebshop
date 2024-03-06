package com.example.hralgebrajavawebshop.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull(message = "User ID must not be null")
    private Integer userId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<@Valid OrderItemDTO> items;

    @NotNull(message = "Total amount must not be null")
    private BigDecimal totalAmount;

    @NotBlank(message = "Payment method must not be empty")
    private String paymentMethod;


    private String paypalTransactionId;

    private String paymentStatus;

}

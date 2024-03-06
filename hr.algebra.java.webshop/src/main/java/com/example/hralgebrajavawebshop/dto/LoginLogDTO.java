package com.example.hralgebrajavawebshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogDTO {

    @NotNull(message="Login time must not be null")
    private Date loginTime;

    @NotBlank(message = "IP address must not be empty")
    private String ipAddress;

    @NotNull(message="User ID must not be null")
    private int userId;
}

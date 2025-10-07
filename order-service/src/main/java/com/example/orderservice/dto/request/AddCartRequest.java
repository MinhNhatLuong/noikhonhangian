package com.example.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartRequest {
    @NotBlank(message = "User ID cannot be blank")
    private Long userId;

    @NotBlank(message = "Product cannot be blank")
    private String product;

    @NotBlank(message = "Price cannot be blank")
    private Double price;
}

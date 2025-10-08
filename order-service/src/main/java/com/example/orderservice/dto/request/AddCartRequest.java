package com.example.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartRequest {

    @NotBlank(message = "Product cannot be blank")
    private String product;

    @NotBlank(message = "Quantity cannot be blank")
    private int quantity;

    @NotBlank(message = "Price cannot be blank")
    private Double price;
}

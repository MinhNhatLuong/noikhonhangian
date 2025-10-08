package com.example.orderservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartResponse {
    private Long id;
    private Long userId;
    private String product;
    private int quantity;
    private Double price;
}

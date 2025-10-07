package com.example.orderservice.service;

import com.example.orderservice.dto.request.AddCartRequest;
import com.example.orderservice.dto.response.AddCartResponse;
import com.example.orderservice.dto.response.RestResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    RestResponse<AddCartResponse> addOrder(AddCartRequest request);
    RestResponse<List<AddCartResponse>> getCartsByUserId(Long userId);
}

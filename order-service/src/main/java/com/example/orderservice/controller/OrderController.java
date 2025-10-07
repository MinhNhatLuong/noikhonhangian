package com.example.orderservice.controller;

import com.example.orderservice.dto.request.AddCartRequest;
import com.example.orderservice.dto.response.AddCartResponse;
import com.example.orderservice.dto.response.RestResponse;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public RestResponse<AddCartResponse> createOrder(@RequestBody AddCartRequest request) {
        return orderService.addOrder(request);
    }

    @GetMapping
    public RestResponse<List<AddCartResponse>> listOrders() {
        log.info("" + JwtUtils.getUserIdFromToken());
        return orderService.getCartsByUserId(JwtUtils.getUserIdFromToken());
    }
}

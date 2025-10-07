package com.example.orderservice.serviceImpl;

import com.example.orderservice.dto.request.AddCartRequest;
import com.example.orderservice.dto.response.AddCartResponse;
import com.example.orderservice.dto.response.RestResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public RestResponse<AddCartResponse> addOrder(AddCartRequest request) {
        Order order = orderMapper.toOrderEntity(request);
        orderRepository.save(order);
        return new RestResponse<AddCartResponse>(200, "Order created successfully!", orderMapper.toAddCartResponse(order));
    }

    @Override
    public RestResponse<List<AddCartResponse>> getCartsByUserId(Long userId) {
        List<AddCartResponse> orders = orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toAddCartResponse)
                .toList();

        return new RestResponse<List<AddCartResponse>>(200, "Get carts by userId successfully!", orders);
    }
}

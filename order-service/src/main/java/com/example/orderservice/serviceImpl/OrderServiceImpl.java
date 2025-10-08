package com.example.orderservice.serviceImpl;

import com.example.orderservice.dto.request.AddCartRequest;
import com.example.orderservice.dto.response.AddCartResponse;
import com.example.orderservice.dto.response.RestResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public RestResponse<AddCartResponse> addOrder(AddCartRequest request) {
        if (JwtUtils.getUserIdFromToken() == null) throw new BadCredentialsException("Invalid user ID");
        Order order = orderMapper.toOrderEntity(request);
        order.setUserId(JwtUtils.getUserIdFromToken());
        orderRepository.save(order);
        return new RestResponse<AddCartResponse>(200, "Order created successfully!", orderMapper.toAddCartResponse(order));
    }

    @Override
    public RestResponse<List<AddCartResponse>> getCartsByUserId(Long userId) {
        if (userId == null) throw new BadCredentialsException("Invalid user ID");

        List<AddCartResponse> orders = orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toAddCartResponse)
                .toList();

        return new RestResponse<List<AddCartResponse>>(200, "Get carts by userId successfully!", orders);
    }
}

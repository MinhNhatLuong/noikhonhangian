package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.AddCartRequest;
import com.example.orderservice.dto.response.AddCartResponse;
import com.example.orderservice.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrderEntity(AddCartRequest request);
    AddCartResponse toAddCartResponse(Order order);
}

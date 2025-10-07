package com.example.orderservice.repository.HttpClient;

import com.example.orderservice.dto.response.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", url = "http://localhost:3979/users")
public interface UserClient {
}

package com.example.apigateway.service;


import com.example.apigateway.dto.request.IntrospectRequest;
import com.example.apigateway.dto.response.IntrospectResponse;
import com.example.apigateway.dto.response.RestResponse;
import com.example.apigateway.repository.IamClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IamService {

    IamClient iamClient;

    public Mono<RestResponse<IntrospectResponse>> introspectToken(String token) {
        return iamClient.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }

}

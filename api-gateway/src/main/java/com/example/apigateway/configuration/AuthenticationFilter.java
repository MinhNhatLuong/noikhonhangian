package com.example.apigateway.configuration;

import com.example.apigateway.dto.response.RestResponse;
import com.example.apigateway.service.IamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final IamService iamService;
    private final ObjectMapper objectMapper;

    @NonFinal
    private String[] whileList = {
            "/auth/.*",
            "/users"
    };

    @NonFinal
    private String[] swaggerList = {
            "/swagger-ui/.*",
            "/v3/api-docs/.*",
            "/swagger-ui.html",
            "/iam/v3/api-docs",
            "/patient/v3/api-docs"
    };

    @Value("${app.prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isInWhiteList(ServerHttpRequest request) {
        return Arrays.stream(whileList)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    private boolean isInSwaggerList(ServerHttpRequest request) {
        return Arrays.stream(swaggerList)
                .anyMatch(s -> request.getURI().getPath().matches(s));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (isInWhiteList(exchange.getRequest()) || isInSwaggerList(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        List<String> auth = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(auth)) {
            log.error("Missing Authorization header");
            return unauthorizedResponse(exchange.getResponse());
        }

        String token = auth.getFirst().replace("Bearer ", "");
        log.info(("Token: " + token));

        return iamService.introspectToken(token).flatMap(introspectResponse -> {
            if (introspectResponse.getResult().isValid()) {
                log.info("result: {}", introspectResponse);
                return chain.filter(exchange);
            } else {
                log.info("result: {}", introspectResponse);
                return unauthorizedResponse(exchange.getResponse());
            }
        }).onErrorResume(throwable -> {
            log.error("Error during introspection", throwable);
            return unauthorizedResponse(exchange.getResponse());
        });

    }

    Mono<Void> unauthorizedResponse(ServerHttpResponse response) {
        RestResponse<?> restResponse = RestResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("You are not authorized to access this resource")
                .result(null)
                .build();

        String body = null;

        try {
            body = objectMapper.writeValueAsString(restResponse);
        } catch (Exception e) {
            log.error("Error while writing unauthorized response", e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

}

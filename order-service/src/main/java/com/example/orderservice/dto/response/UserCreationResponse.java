package com.example.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationResponse {
    private int id;
    private String username;
    private String email;
    private String fullName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
}

package com.rofik.miniproject.domain.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = SnakeCaseStrategy.class)
public class LoginResponse {
    private String username;
    private String role;
    private String tokenType;
    private String token;
}

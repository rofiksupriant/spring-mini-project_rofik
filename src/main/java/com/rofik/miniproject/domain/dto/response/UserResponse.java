package com.rofik.miniproject.domain.dto.response;

import com.rofik.miniproject.domain.common.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String name;

    private String username;

    private String email;

    private UserRole role;

    private Boolean active;
}

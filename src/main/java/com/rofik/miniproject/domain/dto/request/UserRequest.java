package com.rofik.miniproject.domain.dto.request;

import com.rofik.miniproject.domain.common.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequest {
    @Size(min = 5, max = 50)
    private String name;

    @Size(min = 5, max = 25)
    private String username;

    @Min(value = 5)
    private String password;

    @Email
    private String email;

    @NotNull
    private UserRole role;

    @NotNull
    private Boolean active;
}

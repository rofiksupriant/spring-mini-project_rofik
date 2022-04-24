package com.rofik.miniproject.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GreetingRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 25)
    private String name;
}

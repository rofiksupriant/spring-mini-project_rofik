package com.rofik.miniproject.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CategoryRequest {
    @Size(max = 25)
    @NotBlank
    @NotNull
    private String name;

    private String description;
}

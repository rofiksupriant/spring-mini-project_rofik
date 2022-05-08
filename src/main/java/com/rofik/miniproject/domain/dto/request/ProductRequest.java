package com.rofik.miniproject.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductRequest {
    @NotNull
    @NotBlank
    private String name;

    private String description;

    private Integer volume;

    private Integer weight;

    @NotNull
    private Integer price;

}

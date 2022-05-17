package com.rofik.miniproject.domain.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequest {
    @NotNull
    @NotBlank
    private String name;

    private String description;

    private Integer volume;

    private Integer weight;

    @NotNull
    private Integer price;

    private MultipartFile image;

}

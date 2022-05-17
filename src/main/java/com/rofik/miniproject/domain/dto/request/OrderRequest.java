package com.rofik.miniproject.domain.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {
    @NotNull(message = "product must not be null")
    @NotEmpty(message = "product must not be empty")
    private List<ProductOrderRequest> products;

    @NotNull(message = "payment id must not be null")
    private Long paymentId;
}

package com.rofik.miniproject.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductOrderRequest {
    private Long id;
    private Integer quantity;
}

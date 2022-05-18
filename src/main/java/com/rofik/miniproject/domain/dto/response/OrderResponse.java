package com.rofik.miniproject.domain.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rofik.miniproject.domain.common.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {
    private Long id;
    private Integer totalPrice;
    private Integer totalQty;
    private PaymentResponse payment;
    private TableResponse table;
    private OrderStatus status;
}

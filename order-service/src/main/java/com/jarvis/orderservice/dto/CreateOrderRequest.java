package com.jarvis.orderservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    private UUID customerId;
    private BigDecimal totalAmount;
    private String status;

}

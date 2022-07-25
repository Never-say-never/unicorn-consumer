package com.kafka.unicorn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnicornSausage {
    private String unicornId;
    private long amountInGrams;
    private BigDecimal totalPrice;
}
package com.kafka.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sausage {
    private String unicornId;
    private long weightInGrams;
    private int amount;
    private BigDecimal price;
}

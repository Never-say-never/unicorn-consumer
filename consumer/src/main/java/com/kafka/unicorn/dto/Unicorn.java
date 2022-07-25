package com.kafka.unicorn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unicorn {
    private String name;
    private long weightInGrams;
}

package com.kafka.unicorn.service;

import com.kafka.core.domain.Meat;
import com.kafka.core.domain.Unicorn;
import com.kafka.core.domain.Sausage;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

@Service
public class SausageService implements ButcheryService<Meat, Sausage>{

    public static final double SAUSAGE_PER_KILOGRAM_COEFFICIENT = 2.6;
    public static final double SAUSAGE_PRICE_FOR_ONE_GRAM = 18500;
    public static final long MEAT_IN_GRAM_IN_ONE_SAUSAGE = 10;

    public static String unicornId(Unicorn unicorn) {
        return DigestUtils.md5DigestAsHex(unicorn.getName().getBytes());
    }

    public Sausage execute(Meat unicornMeat) {
        final long sausageAmountInGrams = calculateMeatWeightInGrams(unicornMeat);

        return Sausage.builder()
                .unicornId(unicornMeat.getUnicornId())
                .weightInGrams(sausageAmountInGrams)
                .price(calculatePrice(sausageAmountInGrams))
                .amount(sausageAmountInGrams / MEAT_IN_GRAM_IN_ONE_SAUSAGE)
                .build();
    }

    private BigDecimal calculatePrice(long meatAmountInGrams) {
        return BigDecimal.valueOf(meatAmountInGrams * SAUSAGE_PRICE_FOR_ONE_GRAM);
    }

    private long calculateMeatWeightInGrams(Meat unicorn) {
        return Math.round(unicorn.getWeightInGrams() / SAUSAGE_PER_KILOGRAM_COEFFICIENT);
    }
}

package com.kafka.unicorn.service;

import com.kafka.core.domain.Meat;
import com.kafka.core.domain.Unicorn;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

@Service
public class SlaughteringService implements ButcheryService<Unicorn, Meat> {

    public static final double MEAT_PER_KILOGRAM_COEFFICIENT = 4.8;
    public static final double MEAT_PRICE_FOR_ONE_GRAM = 6000;

    public static String unicornId(Unicorn unicorn) {
        return DigestUtils.md5DigestAsHex(unicorn.getName().getBytes());
    }

    public Meat execute(Unicorn unicorn) {
        final long meatAmountInGrams = calculateMeatWeightInGrams(unicorn);

        return Meat.builder()
                .unicornId(unicornId(unicorn))
                .weightInGrams(meatAmountInGrams)
                .price(calculateTotalPrice(meatAmountInGrams))
                .build();
    }

    private BigDecimal calculateTotalPrice(long meatAmountInGrams) {
        return BigDecimal.valueOf(meatAmountInGrams * MEAT_PRICE_FOR_ONE_GRAM);
    }

    private long calculateMeatWeightInGrams(Unicorn unicorn) {
        return Math.round(unicorn.getWeightInGrams() / MEAT_PER_KILOGRAM_COEFFICIENT);
    }
}

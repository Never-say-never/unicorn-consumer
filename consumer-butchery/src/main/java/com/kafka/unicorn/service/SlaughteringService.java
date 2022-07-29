package com.kafka.unicorn.service;

import com.kafka.core.domain.Unicorn;
import com.kafka.core.domain.Meet;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

@Service
public class SlaughteringService implements ButcheryService<Unicorn, Meet> {

    public static final double MEET_PER_KILOGRAM_COEFFICIENT = 4.8;
    public static final double MEET_PRICE_FOR_ONE_GRAM = 6000;

    public static String unicornId(Unicorn unicorn) {
        return DigestUtils.md5DigestAsHex(unicorn.getName().getBytes());
    }

    public Meet execute(Unicorn unicorn) {
        final long meetAmountInGrams = calculateMeetWeightInGrams(unicorn);

        return Meet.builder()
                .unicornId(unicornId(unicorn))
                .weightInGrams(meetAmountInGrams)
                .price(calculateTotalPrice(meetAmountInGrams))
                .build();
    }

    private BigDecimal calculateTotalPrice(long meetAmountInGrams) {
        return BigDecimal.valueOf(meetAmountInGrams * MEET_PRICE_FOR_ONE_GRAM);
    }

    private long calculateMeetWeightInGrams(Unicorn unicorn) {
        return Math.round(unicorn.getWeightInGrams() / MEET_PER_KILOGRAM_COEFFICIENT);
    }
}

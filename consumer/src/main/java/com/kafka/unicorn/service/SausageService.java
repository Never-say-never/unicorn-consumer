package com.kafka.unicorn.service;

import com.kafka.unicorn.dto.Unicorn;
import com.kafka.unicorn.dto.UnicornMeet;
import com.kafka.unicorn.dto.UnicornSausage;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

@Service
public class SausageService {

    public static final double MEET_PER_KILOGRAM_COEFFICIENT = 4.8;
    public static final double MEET_PRICE_FOR_ONE_GRAM = 6000;
    public static final double SAUSAGE_PER_KILOGRAM_COEFFICIENT = 2.6;
    public static final double SAUSAGE_PRICE_FOR_ONE_GRAM = 7500;

    public static String unicornId(Unicorn unicorn) {
        return DigestUtils.md5DigestAsHex(unicorn.getName().getBytes());
    }

    public UnicornMeet processUnicorn(Unicorn unicorn, String id) {
        final long meetAmountInGrams = calculateMeetWeightInGrams(unicorn);
        final BigDecimal totalPrice = calculateTotalPrice(meetAmountInGrams);

        return UnicornMeet.builder()
                .unicornId(id)
                .amountInGrams(meetAmountInGrams)
                .totalPrice(totalPrice)
                .build();
    }

    public UnicornSausage processUnicornMeet(UnicornMeet unicornMeet) {
        final long sausageAmountInGrams = Math.round(unicornMeet.getAmountInGrams() / SAUSAGE_PER_KILOGRAM_COEFFICIENT);
        final BigDecimal totalPrice = BigDecimal.valueOf(sausageAmountInGrams * SAUSAGE_PRICE_FOR_ONE_GRAM);

        return UnicornSausage.builder()
                .unicornId(unicornMeet.getUnicornId())
                .amountInGrams(sausageAmountInGrams)
                .totalPrice(totalPrice)
                .build();
    }

    private BigDecimal calculateTotalPrice(long meetAmountInGrams) {
        return BigDecimal.valueOf(meetAmountInGrams * MEET_PRICE_FOR_ONE_GRAM);
    }

    private long calculateMeetWeightInGrams(Unicorn unicorn) {
        return Math.round(unicorn.getWeightInGrams() / MEET_PER_KILOGRAM_COEFFICIENT);
    }
}

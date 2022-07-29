package com.kafka.unicorn.service;

import com.kafka.core.domain.Unicorn;
import com.kafka.core.domain.Meet;
import com.kafka.core.domain.Sausage;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

@Service
public class SausageService implements ButcheryService<Meet, Sausage>{

    public static final double SAUSAGE_PER_KILOGRAM_COEFFICIENT = 2.6;
    public static final double SAUSAGE_PRICE_FOR_ONE_GRAM = 7500;

    public static String unicornId(Unicorn unicorn) {
        return DigestUtils.md5DigestAsHex(unicorn.getName().getBytes());
    }

    public Sausage execute(Meet unicornMeet) {
        final long sausageAmountInGrams = calculateMeetWeightInGrams(unicornMeet);

        return Sausage.builder()
                .unicornId(unicornMeet.getUnicornId())
                .weightInGrams(sausageAmountInGrams)
                .price(calculatePrice(sausageAmountInGrams))
                .build();
    }

    private BigDecimal calculatePrice(long meetAmountInGrams) {
        return BigDecimal.valueOf(meetAmountInGrams * SAUSAGE_PRICE_FOR_ONE_GRAM);
    }

    private long calculateMeetWeightInGrams(Meet unicorn) {
        return Math.round(unicorn.getWeightInGrams() / SAUSAGE_PER_KILOGRAM_COEFFICIENT);
    }
}

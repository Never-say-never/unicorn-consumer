package com.kafka.unicorn.consumer;

import com.kafka.core.domain.Sausage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;

@Service
@Slf4j
@KafkaListener(topics = "${kafka.delivery.topic.name}")
public class UnicornListener {

    @KafkaHandler
    public void listenOnUnicornSausageReady(Sausage unicorn) {
        log.info("ID: {}, grams: {}, price: {} - sausage delivered to the candy-shop",
                unicorn.getUnicornId(), unicorn.getWeightInGrams(),
                unicorn.getPrice().setScale(2, RoundingMode.CEILING));
    }

    @KafkaHandler(isDefault = true)
    public void handleDefault(ConsumerRecord<String, String> message) {
        log.info("Unicorn default listener, key: {}, message: {}", message.key(), message.value());
    }
}

package com.kafka.unicorn.consumer;

import com.kafka.core.domain.Sausage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;

@Service
@Slf4j
@KafkaListener(topics = "${kafka.topic.name}")
public class UnicornListener {

    private final KafkaTemplate<String, Sausage> sausageKafkaTemplate;
    private final String topic;

    public UnicornListener(KafkaTemplate<String, Sausage> sausageKafkaTemplate,
           @Value("${kafka.topic.name}") String topic) {
        this.sausageKafkaTemplate = sausageKafkaTemplate;
        this.topic = topic;
    }

    @KafkaHandler
    public void listenOnUnicornSausageReady(Sausage unicorn) {
        log.info("ID: {}, grams: {}, price: {} - sausage in market",
                unicorn.getUnicornId(), unicorn.getWeightInGrams(),
                unicorn.getPrice().setScale(2, RoundingMode.CEILING));
    }

    @KafkaHandler(isDefault = true)
    public void handleDefault(ConsumerRecord<String, String> message) {
        log.info("Unicorn default listener, key: {}, message: {}", message.key(), message.value());
    }
}

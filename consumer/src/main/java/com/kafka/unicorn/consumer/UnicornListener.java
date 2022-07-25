package com.kafka.unicorn.consumer;

import com.kafka.unicorn.service.SausageService;
import com.kafka.unicorn.dto.Unicorn;
import com.kafka.unicorn.dto.UnicornMeet;
import com.kafka.unicorn.dto.UnicornSausage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.MathContext;
import java.math.RoundingMode;

import static com.kafka.unicorn.service.SausageService.unicornId;

@Service
@Slf4j
@KafkaListener(topics = "${kafka.topic.name}")
public class UnicornListener {

    private final KafkaTemplate<String, UnicornMeet> kafkaTemplate;
    private final KafkaTemplate<String, UnicornSausage> sausageKafkaTemplate;
    private final SausageService sausageService;
    private final String topic;

    public UnicornListener(KafkaTemplate<String, UnicornMeet> kafkaTemplate,
           KafkaTemplate<String, UnicornSausage> sausageKafkaTemplate,
           SausageService sausageService,
           @Value("${kafka.topic.name}") String topic) {
        this.sausageKafkaTemplate = sausageKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.sausageService = sausageService;
        this.topic = topic;
    }

    @KafkaHandler
    public void listenOnUnicornReady(Unicorn unicorn) {
        String unicornId = unicornId(unicorn);
        log.info("ID: {}, Unicorn: {}, weigh: {} - unicorn dismantling",
                unicornId, unicorn.getName(), unicorn.getWeightInGrams());

        kafkaTemplate.send(topic, sausageService.processUnicorn(unicorn, unicornId));
    }

    @KafkaHandler
    public void listenOnUnicornMeet(UnicornMeet unicornMeet) {
        log.info("ID: {}, weigh:{}, price:{} - unicorn sousaguation",
                unicornMeet.getUnicornId(), unicornMeet.getAmountInGrams(),
                unicornMeet.getTotalPrice().setScale(2, RoundingMode.CEILING));

        sausageKafkaTemplate.send(topic, sausageService.processUnicornMeet(unicornMeet));
    }

    @KafkaHandler
    public void listenOnUnicornSausage(UnicornSausage unicorn) {
        log.info("ID: {}, grams: {}, price: {} - sausage ready",
                unicorn.getUnicornId(), unicorn.getAmountInGrams(),
                unicorn.getTotalPrice().setScale(2, RoundingMode.CEILING));
    }

    @KafkaHandler(isDefault = true)
    public void handleDefault(ConsumerRecord<String, String> message) {
        log.info("Unicorn default listener, key: {}, message: {}", message.key(), message.value());
    }
}

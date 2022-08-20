package com.kafka.unicorn.consumer;

import com.kafka.core.domain.Meat;
import com.kafka.unicorn.service.SausageService;
import com.kafka.core.domain.Unicorn;
import com.kafka.core.domain.Sausage;
import com.kafka.unicorn.service.SlaughteringService;
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
@KafkaListener(topics = "${kafka.distributor.topic.name}")
public class UnicornListener {

    private final KafkaTemplate<String, Meat> kafkaTemplate;
    private final KafkaTemplate<String, Sausage> sausageKafkaTemplate;
    private final SausageService sausageService;
    private final SlaughteringService slaughteringService;
    private final String topic;
    private final String deliveryTopic;

    public UnicornListener(KafkaTemplate<String, Meat> kafkaTemplate,
           KafkaTemplate<String, Sausage> sausageKafkaTemplate,
           SausageService sausageService,
           SlaughteringService slaughteringService,
           @Value("${kafka.distributor.topic.name}") String topic,
           @Value("${kafka.delivery.topic.name}") String deliveryTopic) {
        this.sausageKafkaTemplate = sausageKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.sausageService = sausageService;
        this.slaughteringService = slaughteringService;
        this.topic = topic;
        this.deliveryTopic = deliveryTopic;
    }

    @KafkaHandler
    public void listenOnUnicornReady(Unicorn unicorn) {
        log.info("Unicorn: {}, weigh: {} - ready to dismantling", unicorn.getName(), unicorn.getWeightInGrams());
        Meat meat = slaughteringService.execute(unicorn);
        log.info("Unicorn ID '{}' has been successfully dismantled, meat weigh: {} gm",
                meat.getUnicornId(), meat.getWeightInGrams());

        kafkaTemplate.send(topic, meat);
    }

    @KafkaHandler
    public void listenOnUnicornMeatReady(Meat meat) {
        log.info("Unicorn ID: {}, meat weigh:{}, price:{} - ready to sausaguation",
                meat.getUnicornId(), meat.getWeightInGrams(),
                meat.getPrice().setScale(2, RoundingMode.CEILING));

        Sausage sausage = sausageService.execute(meat);
        log.info("Unicorn ID: {}, has been successfully sausaguated, sausages {}, sausages weigh {} gm, price {}",
                sausage.getUnicornId(), sausage.getAmount(), sausage.getWeightInGrams(),
                sausage.getPrice().setScale(2, RoundingMode.CEILING));

        sausageKafkaTemplate.send(deliveryTopic, sausage);
    }

    @KafkaHandler(isDefault = true)
    public void handleDefault(ConsumerRecord<String, String> message) {
        log.info("Unicorn default listener, key: {}, message: {}", message.key(), message.value());
    }
}

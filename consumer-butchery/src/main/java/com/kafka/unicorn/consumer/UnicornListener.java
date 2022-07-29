package com.kafka.unicorn.consumer;

import com.kafka.unicorn.service.SausageService;
import com.kafka.core.domain.Unicorn;
import com.kafka.core.domain.Meet;
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
@KafkaListener(topics = "${kafka.topic.name}")
public class UnicornListener {

    private final KafkaTemplate<String, Meet> kafkaTemplate;
    private final KafkaTemplate<String, Sausage> sausageKafkaTemplate;
    private final SausageService sausageService;
    private final SlaughteringService slaughteringService;
    private final String topic;

    public UnicornListener(KafkaTemplate<String, Meet> kafkaTemplate,
           KafkaTemplate<String, Sausage> sausageKafkaTemplate,
           SausageService sausageService,
           SlaughteringService slaughteringService,
           @Value("${kafka.topic.name}") String topic) {
        this.sausageKafkaTemplate = sausageKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.sausageService = sausageService;
        this.slaughteringService = slaughteringService;
        this.topic = topic;
    }

    @KafkaHandler
    public void listenOnUnicornReady(Unicorn unicorn) {
        log.info("Unicorn: {}, weigh: {} - unicorn dismantling",
                unicorn.getName(), unicorn.getWeightInGrams());

        kafkaTemplate.send(topic, slaughteringService.execute(unicorn));
    }

    @KafkaHandler
    public void listenOnUnicornMeetReady(Meet meet) {
        log.info("ID: {}, weigh:{}, price:{} - unicorn sousaguation",
                meet.getUnicornId(), meet.getWeightInGrams(),
                meet.getPrice().setScale(2, RoundingMode.CEILING));

        sausageKafkaTemplate.send(topic, sausageService.execute(meet));
    }

    @KafkaHandler(isDefault = true)
    public void handleDefault(ConsumerRecord<String, String> message) {
        log.info("Unicorn default listener, key: {}, message: {}", message.key(), message.value());
    }
}

package com.kafka.unicorn.api;

import com.kafka.core.domain.Unicorn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UnicornController {

    private final KafkaTemplate<String, Unicorn> unicornKafkaTemplate;
    private final String topic;

    public UnicornController(KafkaTemplate<String, Unicorn> unicornKafkaTemplate,
            @Value("${kafka.distributor.topic.name}") String topic) {

        this.unicornKafkaTemplate = unicornKafkaTemplate;
        this.topic = topic;
    }

    @PostMapping("/publish/unicorn")
    public ResponseEntity<?> publishUnicorn(@RequestBody final Unicorn unicorn) {
        log.info("Unicorn ready to sousaguation, publishing...");
        unicornKafkaTemplate.send(topic, unicorn)
                .completable()
                .thenRun(() -> log.info("Unicorn successfully distributed to rainbow: name {}, weight: {} gm",
                        unicorn.getName(), unicorn.getWeightInGrams()));

        return ResponseEntity.ok().build();
    }
}

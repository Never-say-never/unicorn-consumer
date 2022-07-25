package com.kafka.unicorn.api;

import com.kafka.unicorn.dto.Unicorn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j
@RestController
public class UnicornController {

    private final KafkaTemplate<String, Unicorn> unicornKafkaTemplate;
    private final String topic;

    public UnicornController(KafkaTemplate<String, Unicorn> unicornKafkaTemplate,
         @Value("${kafka.topic.name}") String topic) {

        this.unicornKafkaTemplate = unicornKafkaTemplate;
        this.topic = topic;
    }

    @PostMapping("/publish/unicorn")
    public ResponseEntity<?> publishUnicorn(@RequestBody final Unicorn unicorn) {
        log.info("Unicorn ready to sousaguation, publishing...");
        unicornKafkaTemplate.send(topic, unicorn)
                .completable()
                .thenRun(() -> log.info("Unicorn successfully published {}", unicorn));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/publish/bad/unicorn/")
    public ResponseEntity<?> publishUnicorn() {
        log.info("Unicorn ready to sousaguation, publishing...");
        unicornKafkaTemplate.send(topic, new Unicorn("Ms. Duddle", (short) 1));

        return ResponseEntity.ok().build();
    }
}

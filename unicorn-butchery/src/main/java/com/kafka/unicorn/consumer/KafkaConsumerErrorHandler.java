package com.kafka.unicorn.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class KafkaConsumerErrorHandler implements ErrorHandler {

    @Override
    public void handle(Exception thrownException,
                       List<ConsumerRecord<?, ?>> records,
                       Consumer<?, ?> consumer,
                       MessageListenerContainer container) {

        if (thrownException instanceof RecordDeserializationException) {
            log.error("Consumer deserialization error {}", thrownException.getCause().getMessage());

            RecordDeserializationException exception = (RecordDeserializationException) thrownException;
            consumer.seek(exception.topicPartition(), exception.offset() + 1);

        } else {
            log.error("Consumer error {}", thrownException.getCause().getCause().getMessage());
        }
    }

    @Override
    public void handle(Exception exception, ConsumerRecord<?, ?> consumerRecord) {
        if (Objects.isNull(consumerRecord)) {
            log.error("Consumer  error {}", exception.getCause().getMessage());
        }

        log.error("Consumer record: key: {}, value {}, error {}",
                consumerRecord.key(), consumerRecord.value(), exception.getCause().getMessage());
    }
}

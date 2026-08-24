package com.payment.service;

import com.payment.config.KafkaTopics;
import com.payment.dto.LogEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogEventProducer {

    private final KafkaTemplate<String, LogEventMessage> kafkaTemplate;

    public void publish(LogEventMessage event) {

        kafkaTemplate.send(
                KafkaTopics.APPLICATION_LOGS,
                event.traceId(),
                event
        ).whenComplete((result, ex) -> {

            if (ex != null) {
                System.out.println("Kafka publish failed: " + ex.getMessage());
            } else {
                System.out.println(
                        "Kafka message published successfully. Topic: "
                                + result.getRecordMetadata().topic()
                );
            }
        });
    }
}
package com.payment.service;

import com.payment.dto.LogEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final LogEventProducer logEventProducer;

    public String processPayment() {

        String traceId = MDC.get("traceId");

        String message =
                "Redis connection pool exhausted while processing payment";

        log.error(message);

        LogEventMessage event = new LogEventMessage(
                LocalDateTime.now(),
                "payment-service",
                "ERROR",
                traceId,
                message,
                "RedisConnectionException",
                "Simulated stack trace",
                "production"
        );

        logEventProducer.publish(event);

        throw new RuntimeException(message);
    }
}
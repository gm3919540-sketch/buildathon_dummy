package com.payment.service;

import com.payment.dto.LogEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import com.payment.exception.RedisConnectionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final LogEventProducer logEventProducer;

    public String processPayment() {

        String traceId = MDC.get("traceId");

        try {
            simulatePaymentFailure();

            return "Payment processed successfully";

        } catch (Exception exception) {

            log.error(
                    "Payment processing failed",
                    exception
            );

            String stackTrace = getStackTrace(exception);

            LogEventMessage event = new LogEventMessage(
                    LocalDateTime.now(),
                    "payment-service",
                    "ERROR",
                    traceId,
                    exception.getMessage(),
                    exception.getClass().getSimpleName(),
                    stackTrace,
                    "production"
            );

            logEventProducer.publish(event);

            throw exception;
        }
    }

    private void simulatePaymentFailure() {

        throw new RedisConnectionException(
                "Redis connection pool exhausted while processing payment"
        );
    }

    private String getStackTrace(Exception exception) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        exception.printStackTrace(printWriter);

        return stringWriter.toString();
    }
}
package com.payment.service;

import com.payment.dto.LogEventMessage;
import com.payment.exception.BankServiceException;
import com.payment.exception.DatabaseConnectionException;
import com.payment.exception.PaymentGatewayTimeoutException;
import com.payment.exception.RedisConnectionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final LogEventProducer logEventProducer;


    public String processPayment(
            String failureType
    ) {

        String traceId = MDC.get("traceId");

        try {

            simulatePaymentFailure(
                    failureType
            );

            return "Payment processed successfully";

        } catch (Exception exception) {

            log.error(
                    "Payment processing failed",
                    exception
            );

            String stackTrace =
                    getStackTrace(exception);

            LogEventMessage event =
                    new LogEventMessage(
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


    private void simulatePaymentFailure(
            String failureType
    ) {

        if (failureType == null) {

            throw new IllegalArgumentException(
                    "Failure type cannot be null"
            );
        }


        switch (
                failureType.toUpperCase()
        ) {

            case "REDIS":

                throw new RedisConnectionException(
                        "Redis connection pool exhausted while processing payment"
                );


            case "DATABASE":

                throw new DatabaseConnectionException(
                        "Database connection timeout while saving payment transaction"
                );


            case "GATEWAY":

                throw new PaymentGatewayTimeoutException(
                        "Payment gateway timeout while processing payment"
                );


            case "BANK":

                throw new BankServiceException(
                        "Bank API returned 503 Service Unavailable"
                );


            default:

                throw new IllegalArgumentException(
                        "Unknown payment failure type: "
                                + failureType
                );
        }
    }


    private String getStackTrace(
            Exception exception
    ) {

        StringWriter stringWriter =
                new StringWriter();

        PrintWriter printWriter =
                new PrintWriter(
                        stringWriter
                );

        exception.printStackTrace(
                printWriter
        );

        return stringWriter.toString();
    }
}
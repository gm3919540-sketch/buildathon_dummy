package com.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

    public String processPayment() {

        log.error("Payment processing failed: Redis connection pool exhausted");

        throw new RuntimeException(
                "Redis connection pool exhausted while processing payment"
        );
    }
}
package com.payment.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping
    public String processPayment() {
        return "Payment processed successfully";
    }
}
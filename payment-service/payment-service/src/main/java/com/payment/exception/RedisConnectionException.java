package com.payment.exception;

public class RedisConnectionException extends RuntimeException {

    public RedisConnectionException(String message) {
        super(message);
    }
}
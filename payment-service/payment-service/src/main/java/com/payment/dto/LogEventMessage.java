package com.payment.dto;

import java.time.LocalDateTime;

public record LogEventMessage(
        LocalDateTime timestamp,
        String serviceName,
        String level,
        String traceId,
        String message,
        String exceptionType,
        String stackTrace,
        String environment
) {
}
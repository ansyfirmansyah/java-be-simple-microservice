package com.test.dto.response;

import java.time.Instant;

public record ApiResponse<T>(
        String message,
        T body,
        Instant timestamp
) {
    public ApiResponse(String message, T body) {
        this(message, body, Instant.now());
    }
}

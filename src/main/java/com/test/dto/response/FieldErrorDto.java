package com.test.dto.response;

public record FieldErrorDto (
        String field,
        String message
) {
}

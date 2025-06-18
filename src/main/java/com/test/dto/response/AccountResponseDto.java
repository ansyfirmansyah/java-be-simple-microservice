package com.test.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String ownerName,
        String email,
        LocalDate dateOfBirth,
        String address,
        BigDecimal balance,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

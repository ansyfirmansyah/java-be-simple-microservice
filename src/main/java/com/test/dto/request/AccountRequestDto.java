package com.test.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountRequestDto(
        @NotBlank(message = "ownerName.required")
        String ownerName,

        @Email(message = "email.invalid")
        @NotBlank(message = "email.required")
        String email,

        @Past(message = "dateOfBirth.past")
        LocalDate dateOfBirth,

        @NotBlank(message = "address.required")
        String address,

        @NotNull(message = "initialBalance.required")
        @DecimalMin(value = "0.0", inclusive = true, message = "initialBalance.min")
        BigDecimal initialBalance
) {
}

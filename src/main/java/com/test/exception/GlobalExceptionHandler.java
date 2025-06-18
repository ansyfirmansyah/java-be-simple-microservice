package com.test.exception;

import com.test.dto.response.ApiResponse;
import com.test.dto.response.FieldErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    /**
     * Handle ResponseStatusException (eg. NOT_FOUND)
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException ex) {
        ApiResponse<Void> body = new ApiResponse<>(ex.getReason(), null);
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    /**
     * Handle @Valid validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldErrorDto>>> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldErrorDto> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldErrorDto(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("validation.failed", null, LocaleContextHolder.getLocale());
        ApiResponse<List<FieldErrorDto>> body = new ApiResponse<>(msg, errors);
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Fallback untuk semua exception lain (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception ex) {
        ApiResponse<Void> body = new ApiResponse<>("Internal server error", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

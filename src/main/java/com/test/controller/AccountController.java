package com.test.controller;

import com.test.dto.request.AccountRequestDto;
import com.test.dto.response.AccountResponseDto;
import com.test.dto.response.ApiResponse;
import com.test.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
@Validated
public class AccountController {
    private final AccountService accountService;

    /**
     * READ (paginated)
     * GET /accounts?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AccountResponseDto>>> getAllAccount(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountResponseDto> result = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(new ApiResponse<>("List Account", result));
    }

    /**
     * CREATE Account
     * POST /accounts → 201 Created + Location header
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponseDto>> createAccount(
            @RequestBody @Valid AccountRequestDto req) {

        AccountResponseDto dto = accountService.createAccount(req);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse<>("Account created", dto));
    }

    /**
     * READ by ID
     * GET /accounts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponseDto>> getAccountById(
            @PathVariable UUID id) {

        AccountResponseDto dto = accountService.getAccountById(id);
        return ResponseEntity.ok(new ApiResponse<>("Account details", dto));
    }

    /**
     * UPDATE Account
     * PUT /accounts/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponseDto>> updateAccount(
            @PathVariable UUID id,
            @RequestBody @Valid AccountRequestDto req) {

        AccountResponseDto dto = accountService.updateAccount(id, req);
        return ResponseEntity.ok(new ApiResponse<>("Account updated", dto));
    }

    /**
     * DELETE Account
     * DELETE /accounts/{id} → 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}

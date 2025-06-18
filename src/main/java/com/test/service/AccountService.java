package com.test.service;

import com.test.dto.request.AccountRequestDto;
import com.test.dto.response.AccountResponseDto;
import com.test.entity.Account;
import com.test.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MessageSource messageSource;

    @Transactional // memastikan atomicity
    public AccountResponseDto createAccount(AccountRequestDto req) {
        Account account = Account.builder()
                .ownerName(req.ownerName())
                .email(req.email())
                .dateOfBirth(req.dateOfBirth())
                .address(req.address())
                .balance(req.initialBalance())
                .active(true)
                .createdBy("system") // belum pakai security context
                .createdAt(LocalDateTime.now())
                .build();

        Account saved = accountRepository.save(account);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public AccountResponseDto getAccountById(UUID id) {
        Account acc = accountRepository.findById(id).orElseThrow(() -> {
            String msg = messageSource.getMessage(
                    "account.not.found", null, LocaleContextHolder.getLocale());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        });
        return toDto(acc);
    }

    @Transactional
    public AccountResponseDto updateAccount(UUID id, AccountRequestDto req) {
        Account acc = accountRepository.findById(id).orElseThrow(() -> {
            String msg = messageSource.getMessage(
                    "account.not.found", null, LocaleContextHolder.getLocale());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        });

        acc.setOwnerName(req.ownerName());
        acc.setEmail(req.email());
        acc.setDateOfBirth(req.dateOfBirth());
        acc.setAddress(req.address());
        // balance tidak di‐update lewat ini
        // audit:
        acc.setUpdatedBy("system");     // belum pakai security context
        acc.setUpdatedAt(LocalDateTime.now());

        Account updated = accountRepository.save(acc);
        return toDto(updated);
    }

    @Transactional
    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            String msg = messageSource.getMessage(
                    "account.not.found", null, LocaleContextHolder.getLocale());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        }
        accountRepository.deleteById(id);
    }

    private AccountResponseDto toDto(Account acc) {
        return new AccountResponseDto(
                acc.getId(),
                acc.getOwnerName(),
                acc.getEmail(),
                acc.getDateOfBirth(),
                acc.getAddress(),
                acc.getBalance(),
                acc.getActive(),
                acc.getCreatedAt(),
                acc.getUpdatedAt()
        );
    }
}

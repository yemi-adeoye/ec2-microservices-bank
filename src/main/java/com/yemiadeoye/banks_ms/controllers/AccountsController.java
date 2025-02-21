package com.yemiadeoye.banks_ms.controllers;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yemiadeoye.banks_ms.dtos.AccountActiveDto;
import com.yemiadeoye.banks_ms.dtos.request.AccountRequestDto;
import com.yemiadeoye.banks_ms.dtos.response.AccountResponseDto;
import com.yemiadeoye.banks_ms.services.IAccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountsController {
    private final IAccountService accountService;

    public AccountsController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/{accountId}", produces = "application/json")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable String accountId)
            throws AccountNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(accountId));
    }

    @GetMapping(value = "/user/{userId}", produces = "application/json")
    public List<AccountResponseDto> getUserAccounts(@PathVariable String userId) {
        return accountService.getUserAccounts(userId);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto accountDto)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> updateAccount(@PathVariable String accountNumber,
            @RequestBody AccountActiveDto accountActive)
            throws AccountNotFoundException {
        return new ResponseEntity<>(accountService.updateAccount(accountNumber, accountActive), HttpStatus.OK);
    }

    @PutMapping("/user/{userId}")
    public List<AccountResponseDto> updateUserAccount(@PathVariable String userId,
            @RequestBody AccountActiveDto accountActive) {
        return accountService.updateUserAccounts(userId, accountActive);
    }

    @DeleteMapping("/{accountNumber}")
    public AccountResponseDto deleteAccount(@PathVariable String accountNumber) throws AccountNotFoundException {
        return accountService.deleteAccount(accountNumber);
    }

    @DeleteMapping("/user/{userId}")
    public List<AccountResponseDto> deleteUserAccounts(@PathVariable String userId)
            throws AccountNotFoundException {
        return accountService.deleteUserAccounts(userId);
    }
}

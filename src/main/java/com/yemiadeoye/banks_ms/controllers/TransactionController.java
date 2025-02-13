package com.yemiadeoye.banks_ms.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yemiadeoye.banks_ms.dtos.TransactionRequestDto;
import com.yemiadeoye.banks_ms.dtos.TransactionResponseDto;
import com.yemiadeoye.banks_ms.entities.TransactionsEntity;
import com.yemiadeoye.banks_ms.services.ITransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionsEntity>> getTransactions(@PathVariable String accountNumber,
            @RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(transactionService.getTransactions(accountNumber, size, page), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<TransactionResponseDto> postTransaction(
            @Valid @RequestBody TransactionRequestDto transactionRequestDto) throws Exception {

        return new ResponseEntity<>(transactionService.recordTransaction(transactionRequestDto), HttpStatus.CREATED);
    }

}

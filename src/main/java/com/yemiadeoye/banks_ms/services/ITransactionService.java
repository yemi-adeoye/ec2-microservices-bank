package com.yemiadeoye.banks_ms.services;

import java.util.List;

import com.yemiadeoye.banks_ms.dtos.TransactionRequestDto;
import com.yemiadeoye.banks_ms.dtos.TransactionResponseDto;
import com.yemiadeoye.banks_ms.entities.TransactionsEntity;

public interface ITransactionService {

    public TransactionResponseDto recordTransaction(TransactionRequestDto transactionDto) throws Exception;

    public void processTransactions();

    public boolean validateTransaction(TransactionRequestDto transaction) throws Exception;

    public List<TransactionsEntity> getTransactions(String accountNumber, int size, int page);
}

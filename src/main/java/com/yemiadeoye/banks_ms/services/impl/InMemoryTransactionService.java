package com.yemiadeoye.banks_ms.services.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yemiadeoye.banks_ms.dtos.TransactionRequestDto;
import com.yemiadeoye.banks_ms.dtos.TransactionResponseDto;
import com.yemiadeoye.banks_ms.entities.AccountsEntity;
import com.yemiadeoye.banks_ms.entities.NotificationEntity;
import com.yemiadeoye.banks_ms.entities.TransactionsEntity;
import com.yemiadeoye.banks_ms.enums.TransactionDescription;
import com.yemiadeoye.banks_ms.enums.TransactionState;
import com.yemiadeoye.banks_ms.enums.TransactionType;
import com.yemiadeoye.banks_ms.exceptions.AccountNotFoundException;
import com.yemiadeoye.banks_ms.repositories.IAccountRepository;
import com.yemiadeoye.banks_ms.repositories.INotificationRepository;
import com.yemiadeoye.banks_ms.repositories.ITransactionRepository;
import com.yemiadeoye.banks_ms.services.ITransactionService;

@Service
public class InMemoryTransactionService implements ITransactionService {

    Map<String, TransactionRequestDto> transactionStore = new HashMap<>();

    @Autowired
    INotificationRepository notificationRepository;

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;
    private final ConversionService conversionService;

    public InMemoryTransactionService(IAccountRepository accountRepository,
            ITransactionRepository transactionRepository,
            ConversionService conversionService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.conversionService = conversionService;
    }

    @Override
    public TransactionResponseDto recordTransaction(TransactionRequestDto transactionRequestDto) throws Exception {
        // validate transaction
        boolean isValidTransaction = validateTransaction(transactionRequestDto);

        if (isValidTransaction) {
            transactionStore.put(UUID.randomUUID().toString(), transactionRequestDto);
        }

        return new TransactionResponseDto(isValidTransaction, transactionRequestDto);
    }

    @Override
    @Scheduled(fixedRate = 5000)
    public void processTransactions() {

        List<String> processedTransactions = transactionStore.entrySet()
                .stream()
                .map((entry) -> processTransaction(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        processedTransactions.stream().forEach((processedId) -> transactionStore.remove(processedId));

        if (notificationRepository.count() == 0) {
            var notification = NotificationEntity.builder()
                    .userId("'userID'")
                    .isRead(false)
                    .message("$400 DEPOSITED TO ACCOUNT ******4578")
                    .build();

            notificationRepository.save(notification);
            System.out.println("--NOTICATION SAVED--");
        }

    }

    private String processTransaction(String transactionRequestId, TransactionRequestDto transactionRequestDto) {
        AccountsEntity beneficiaryAccount = accountRepository
                .findByAccountNumber(transactionRequestDto.beneficiaryAccount());

        AccountsEntity initiatorAccount = accountRepository
                .findByAccountNumber(transactionRequestDto.initiatorAccount());

        TransactionDescription transactionDescription = TransactionDescription
                .valueOf(transactionRequestDto.transactionDescription());

        BigDecimal previousBalance = beneficiaryAccount.getAccountBalance();

        switch (transactionDescription) {
            case ATM_DEPOSIT, OVER_COUNTER_DEPOSIT, DIRECT_DEPOSIT -> {
                TransactionsEntity transaction = conversionService.convert(transactionRequestDto,
                        TransactionsEntity.class);
                transaction.setTransactionType(TransactionType.DEPOSIT.toString());

                // INCREASE BENEFICIARY ACCOUNT BALANCE

                beneficiaryAccount.setAccountBalance(previousBalance.add(transactionRequestDto.amount()));

                // UPDATE TRANSACTION STATUS
                try {
                    accountRepository.save(beneficiaryAccount);

                    transaction.setTransactionStatus(TransactionState.COMPLETED.toString());

                    transaction.setNotes("DEPOSIT OK");

                    transaction.setCompletedAt(new Timestamp(System.currentTimeMillis()));

                } catch (Exception e) {
                    // TODO: handle exception
                    transaction.setTransactionStatus(TransactionState.FAILED.toString());

                    transaction.setNotes("DEPOSIT FAILED");

                    transaction.setCompletedAt(new Timestamp(System.currentTimeMillis()));
                }

                // WRITE TRANSACTION TO DB
                transactionRepository.save(transaction);

                // SEND SOCKET UPDATE TO UI
            }

            case OVER_COUNTER_WITHDRAWAL, ATM_WITHDRAWAL -> {
                TransactionsEntity transaction = conversionService.convert(transactionRequestDto,
                        TransactionsEntity.class);
                beneficiaryAccount.setAccountBalance(previousBalance.subtract(transactionRequestDto.amount()));

                transaction.setTransactionType(TransactionType.WITHDRAWAL.toString());

                try {
                    accountRepository.save(beneficiaryAccount);

                    transaction.setTransactionStatus(TransactionState.COMPLETED.toString());

                    transaction.setNotes("WITHDRAWAL OK");

                    transaction.setCompletedAt(new Timestamp(System.currentTimeMillis()));

                } catch (Exception e) {
                    transaction.setTransactionStatus(TransactionState.FAILED.toString());

                    transaction.setNotes("WITHDRAWAL FAILED");

                    transaction.setCompletedAt(new Timestamp(System.currentTimeMillis()));
                }

                transactionRepository.save(transaction);
            }

            case INTRABANK_TRANSFER -> {
                beneficiaryAccount.setAccountBalance(previousBalance.add(transactionRequestDto.amount()));
                BigDecimal initatorPreviousBalance = initiatorAccount.getAccountBalance();

                initiatorAccount.setAccountBalance(initatorPreviousBalance.subtract(transactionRequestDto.amount()));

                TransactionsEntity transactionDeposit = conversionService.convert(transactionRequestDto,
                        TransactionsEntity.class);

                TransactionsEntity transactionWithdrawal = conversionService.convert(transactionRequestDto,
                        TransactionsEntity.class);

                transactionWithdrawal.setBeneficiary(transactionRequestDto.initiator());
                transactionWithdrawal.setBeneficiaryAccount(transactionRequestDto.initiatorAccount());
                transactionWithdrawal.setTransactionType(TransactionType.WITHDRAWAL.toString());

                transactionDeposit.setBeneficiary(transactionRequestDto.beneficiary());
                transactionDeposit.setTransactionType(TransactionType.DEPOSIT.toString());

                try {
                    accountRepository.saveAll(List.of(beneficiaryAccount, initiatorAccount));

                    transactionWithdrawal.setTransactionStatus(TransactionState.COMPLETED.toString());
                    transactionWithdrawal.setCompletedAt(new Timestamp(System.currentTimeMillis()));
                    transactionWithdrawal.setNotes("WITHDRAWAL OK");

                    transactionDeposit.setTransactionStatus(TransactionState.COMPLETED.toString());
                    transactionDeposit.setCompletedAt(new Timestamp(System.currentTimeMillis()));
                    transactionDeposit.setNotes("DEPOSIT OK");

                } catch (Exception e) {
                    transactionWithdrawal.setTransactionStatus(TransactionState.FAILED.toString());
                    transactionWithdrawal.setCompletedAt(new Timestamp(System.currentTimeMillis()));
                    transactionWithdrawal.setNotes("WITHDRAWAL FAILED");

                    transactionDeposit.setTransactionStatus(TransactionState.FAILED.toString());
                    transactionDeposit.setCompletedAt(new Timestamp(System.currentTimeMillis()));
                    transactionDeposit.setNotes("DEPOSIT FAILED");
                }

                transactionRepository.saveAll(List.of(transactionDeposit, transactionWithdrawal));

            }

            default -> System.out.println(""); // TODO fix this
        }

        return transactionRequestId;
    }

    @Override
    public boolean validateTransaction(TransactionRequestDto transactionRequestDto) throws Exception {
        // get initiator and beneficiary accounts; if not valid exception would be
        // thrown
        AccountsEntity beneficiaryAccount = accountRepository
                .findByAccountNumber(transactionRequestDto.beneficiaryAccount());

        AccountsEntity initiatorAccount = accountRepository
                .findByAccountNumber(transactionRequestDto.initiatorAccount());

        if (initiatorAccount == null || beneficiaryAccount == null) {
            throw new AccountNotFoundException("Invalid initiator and (or) beneficiary account");
        }

        TransactionDescription transactionDescription = TransactionDescription
                .valueOf(transactionRequestDto.transactionDescription());

        switch (transactionDescription) {
            case INTRABANK_TRANSFER -> {
                // 'account from' must be different from 'account to'
                if (transactionRequestDto.initiatorAccount().equals(transactionRequestDto.beneficiaryAccount())) {
                    throw new Exception("Beneficiary account must be different from initiator account");
                }

                // ensure initiator has sufficient balance
                if (initiatorAccount.getAccountBalance().compareTo(transactionRequestDto.amount()) == -1) {
                    throw new Exception("Insufficient funds");
                }
            }
            case ATM_DEPOSIT,
                    OVER_COUNTER_DEPOSIT,
                    DIRECT_DEPOSIT -> {
                // TODO - do something
            }

            case
                    ATM_WITHDRAWAL,
                    OVER_COUNTER_WITHDRAWAL -> {
                // ensure initiator has sufficient balance
                if (initiatorAccount.getAccountBalance().compareTo(transactionRequestDto.amount()) == -1) {
                    throw new Exception("Insufficient funds");
                }

                // intiator account must be same as beneficiary account
                if (!initiatorAccount.getAccountNumber().equals(transactionRequestDto.initiatorAccount())
                        || !beneficiaryAccount.getAccountNumber().equals(transactionRequestDto.beneficiaryAccount())) {
                    throw new Exception("Insufficient Privilege");
                }
            }
            default -> throw new Exception("Invalid Transaction Description");
        }

        // (ensure the initator account belongs to initiator
        if (transactionDescription != TransactionDescription.DIRECT_DEPOSIT) {

            if (!initiatorAccount.getUserId().equals(transactionRequestDto.initiator())) {
                throw new Exception("Insufficient Privilege");
            }
        }

        // ensure beneficiary account belongs to beneficiary
        if (!transactionRequestDto.beneficiary().equals(beneficiaryAccount.getUserId())) {
            throw new Exception("Insufficient Privilege");
        }

        return true;
    }

    public List<TransactionsEntity> getTransactions(String accountNumber, int size, int page) {
        Page<TransactionsEntity> transactionPage = transactionRepository
                .findAllByBeneficiaryAccount(accountNumber,
                        PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "completedAt"))); // -1 because
        // pages are 0
        // based

        return transactionPage.getContent();

    }
}

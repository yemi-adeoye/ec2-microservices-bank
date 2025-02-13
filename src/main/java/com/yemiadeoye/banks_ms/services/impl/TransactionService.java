package com.yemiadeoye.banks_ms.services.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import com.yemiadeoye.banks_ms.entities.RedisTransactionEntity;
import com.yemiadeoye.banks_ms.entities.TransactionsEntity;
import com.yemiadeoye.banks_ms.enums.TransactionDescription;
import com.yemiadeoye.banks_ms.enums.TransactionState;
import com.yemiadeoye.banks_ms.enums.TransactionType;
import com.yemiadeoye.banks_ms.exceptions.AccountNotFoundException;
import com.yemiadeoye.banks_ms.repositories.IAccountRepository;
import com.yemiadeoye.banks_ms.repositories.IRedisTransactionRepository;
import com.yemiadeoye.banks_ms.repositories.ITransactionRepository;
import com.yemiadeoye.banks_ms.services.ITransactionService;

@Service
public class TransactionService implements ITransactionService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;
    private final ConversionService conversionService;
    private final IRedisTransactionRepository redisTransactionRepository;
    private final NotificationService notificationService;

    public TransactionService(IAccountRepository accountRepository, ITransactionRepository transactionRepository,
            ConversionService conversionService, IRedisTransactionRepository redisTransactionRepository,
            NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.conversionService = conversionService;
        this.redisTransactionRepository = redisTransactionRepository;
        this.notificationService = notificationService;
    }

    @Override
    public TransactionResponseDto recordTransaction(TransactionRequestDto transactionRequestDto) throws Exception {
        // validate transaction
        boolean isValidTransaction = validateTransaction(transactionRequestDto);

        if (isValidTransaction) {
            redisTransactionRepository
                    .save(conversionService.convert(transactionRequestDto, RedisTransactionEntity.class));
        }

        return new TransactionResponseDto(isValidTransaction, transactionRequestDto);
    }

    @Override
    @Scheduled(fixedRate = 5000)
    public void processTransactions() {

        List<RedisTransactionEntity> transactionsToBeProcessed = new ArrayList<>();

        redisTransactionRepository.findAll().forEach(transaction -> transactionsToBeProcessed.add(transaction));

        transactionsToBeProcessed.stream().forEach((transaction) -> {
            processTransaction(transaction.getId(), transaction);

            // String beneficiary =
            // accountRepository.findByAccountNumber(transaction.getBeneficiaryAccount()).getUserId();

            System.out.println(transaction);

            redisTransactionRepository.delete(transaction);
        });

    }

    private String processTransaction(String transactionRequestId, RedisTransactionEntity redisTransactionEntity) {
        AccountsEntity beneficiaryAccount = accountRepository
                .findByAccountNumber(redisTransactionEntity.getBeneficiaryAccount());

        AccountsEntity initiatorAccount = accountRepository
                .findByAccountNumber(redisTransactionEntity.getInitiatorAccount());

        TransactionDescription transactionDescription = TransactionDescription
                .valueOf(redisTransactionEntity.getTransactionDescription());

        BigDecimal previousBalance = beneficiaryAccount.getAccountBalance();

        switch (transactionDescription) {

            case ATM_DEPOSIT, OVER_COUNTER_DEPOSIT, DIRECT_DEPOSIT -> {
                processDepositTransaction(redisTransactionEntity, beneficiaryAccount);
            }

            case OVER_COUNTER_WITHDRAWAL, ATM_WITHDRAWAL -> {
                TransactionsEntity transaction = conversionService.convert(
                        redisTransactionEntity,
                        TransactionsEntity.class);
                beneficiaryAccount.setAccountBalance(previousBalance.subtract(redisTransactionEntity.getAmount()));

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

                var transactionDb = transactionRepository.save(transaction);

            }

            case INTRABANK_TRANSFER -> {
                beneficiaryAccount.setAccountBalance(previousBalance.add(redisTransactionEntity.getAmount()));
                BigDecimal initatorPreviousBalance = initiatorAccount.getAccountBalance();

                initiatorAccount
                        .setAccountBalance(initatorPreviousBalance.subtract(redisTransactionEntity.getAmount()));

                TransactionsEntity transactionDeposit = conversionService.convert(
                        redisTransactionEntity,
                        TransactionsEntity.class);

                TransactionsEntity transactionWithdrawal = conversionService.convert(
                        redisTransactionEntity,
                        TransactionsEntity.class);

                transactionWithdrawal.setBeneficiary(redisTransactionEntity.getInitiator());
                transactionWithdrawal.setBeneficiaryAccount(redisTransactionEntity.getInitiatorAccount());
                transactionWithdrawal.setTransactionType(TransactionType.WITHDRAWAL.toString());

                transactionDeposit.setBeneficiary(redisTransactionEntity.getBeneficiary());
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

    private void createAndSendNotification(List<TransactionsEntity> transactions) {

        transactions.stream().forEach(transaction -> {
            String message = notificationService.getNotificationMessage(transaction);

            var notification = NotificationEntity.builder()
                    .userId(transaction.getBeneficiary())
                    .isRead(false)
                    .message(message)
                    .build();

            notificationService.saveNotification(notification);

            notificationService.sendNotification("notifications",
                    message);

        });

    }

    private void processDepositTransaction(RedisTransactionEntity redisTransactionEntity,
            AccountsEntity beneficiaryAccount) {
        TransactionsEntity transaction = conversionService.convert(
                redisTransactionEntity,
                TransactionsEntity.class);
        transaction.setTransactionType(TransactionType.DEPOSIT.toString());

        BigDecimal previousBalance = beneficiaryAccount.getAccountBalance();

        // INCREASE BENEFICIARY ACCOUNT BALANCE
        beneficiaryAccount.setAccountBalance(previousBalance.add(redisTransactionEntity.getAmount()));

        // UPDATE TRANSACTION STATUS
        try {
            accountRepository.save(beneficiaryAccount);

            transaction.setTransactionStatus(TransactionState.COMPLETED.toString());

            transaction.setNotes("DEPOSIT OK");

            transaction.setCompletedAt(new Timestamp(System.currentTimeMillis()));

        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionState.FAILED.toString());

            transaction.setNotes("DEPOSIT FAILED");

            transaction.setCompletedAt(new Timestamp(System.currentTimeMillis()));
        }

        // WRITE TRANSACTION TO DB
        var transactionDb = transactionRepository.save(transaction);

        createAndSendNotification(List.of(transactionDb));

    }

    private void processWithdrawalTransaction() {

    }

    private void processTransaferTransaction() {

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

    @Override
    public List<TransactionsEntity> getTransactions(String accountNumber, int size, int page) {
        Page<TransactionsEntity> transactionPage = transactionRepository
                .findAllByBeneficiaryAccount(accountNumber,
                        PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "completedAt"))); // -1 because
        // pages are 0
        // based

        return transactionPage.getContent();

    }

}

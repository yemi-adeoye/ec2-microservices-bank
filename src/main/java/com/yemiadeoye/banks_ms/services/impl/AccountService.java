package com.yemiadeoye.banks_ms.services.impl;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.yemiadeoye.banks_ms.dtos.AccountActiveDto;
import com.yemiadeoye.banks_ms.dtos.request.AccountRequestDto;
import com.yemiadeoye.banks_ms.dtos.response.AccountResponseDto;
import com.yemiadeoye.banks_ms.entities.AccountsEntity;
import com.yemiadeoye.banks_ms.enums.AccountType;
import com.yemiadeoye.banks_ms.exceptions.AccountNotFoundException;
import com.yemiadeoye.banks_ms.exceptions.InvalidAccountTypeException;
import com.yemiadeoye.banks_ms.repositories.IAccountRepository;
import com.yemiadeoye.banks_ms.services.IAccountService;

@Service
public class AccountService implements IAccountService {

    private final IAccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final ConversionService conversionService;

    public AccountService(IAccountRepository accountRepository, ConversionService conversionService) {
        this.accountRepository = accountRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<AccountResponseDto> getUserAccounts(String userId) {
        List<AccountsEntity> accountsEntity = accountRepository.findAllByUserId(userId);

        return accountsEntity
                .stream()
                .map(account -> conversionService.convert(account, AccountResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDto> deleteUserAccounts(String userId) {

        accountRepository.deleteAllByUserId(userId);

        return accountRepository.findAllByUserId(userId)
                .stream()
                .map(account -> conversionService.convert(account, AccountResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDto> updateUserAccounts(String userId, AccountActiveDto accountActiveDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserAccounts'");
    }

    @Override
    public AccountResponseDto getAccount(String accountId) throws AccountNotFoundException {

        AccountsEntity account = accountRepository.findByAccountNumber(accountId);

        if (account == null) {
            throw new AccountNotFoundException("Account not Found");
        }

        return conversionService.convert(account, AccountResponseDto.class);
    }

    @Override
    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) throws Exception {

        try {
            AccountType.valueOf(accountRequestDto.accountType().toUpperCase());
        } catch (Exception e) {
            throw new InvalidAccountTypeException("Invalid AccountType Supplied");
        }

        AccountsEntity accountEntity = conversionService.convert(accountRequestDto, AccountsEntity.class);

        String accountNumber = generateAccountNumber();

        accountEntity.setAccountNumber(accountNumber);

        AccountsEntity accountEntityDb = accountRepository.save(accountEntity);

        return conversionService.convert(accountEntityDb, AccountResponseDto.class);
    }

    @Override
    public AccountResponseDto updateAccount(String accountNumber, AccountActiveDto accountActiveDto)
            throws AccountNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccount'");
    }

    @Override
    public AccountResponseDto deleteAccount(String accountId) throws AccountNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccount'");
    }

    private String generateAccountNumber() {
        String accountNumber = UUID.randomUUID().toString();
        long MIN_ACCOUNT_NUMBER = 1_000_000_000L;
        long MAN_ACCOUNT_NUMBER = 9_999_999_999L;

        Random random = new Random();

        accountNumber = "" + (random.nextLong(MAN_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER) + MIN_ACCOUNT_NUMBER);

        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = "" + (random.nextLong(MAN_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER) + MIN_ACCOUNT_NUMBER);
        }

        System.out.println(accountNumber);

        return accountNumber;

    }
}

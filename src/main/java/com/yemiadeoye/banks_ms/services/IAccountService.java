package com.yemiadeoye.banks_ms.services;

import java.util.List;

import com.yemiadeoye.banks_ms.dtos.AccountActiveDto;
import com.yemiadeoye.banks_ms.dtos.request.AccountRequestDto;
import com.yemiadeoye.banks_ms.dtos.response.AccountResponseDto;
import com.yemiadeoye.banks_ms.exceptions.AccountNotFoundException;

public interface IAccountService {
    List<AccountResponseDto> getUserAccounts(String userId);

    List<AccountResponseDto> deleteUserAccounts(String userId);

    List<AccountResponseDto> updateUserAccounts(String userId, AccountActiveDto accountActiveDto);

    AccountResponseDto getAccount(String accountId) throws AccountNotFoundException;

    AccountResponseDto createAccount(AccountRequestDto account) throws Exception;

    AccountResponseDto updateAccount(String accountNumber, AccountActiveDto accountActiveDto)
            throws AccountNotFoundException;

    AccountResponseDto deleteAccount(String accountId) throws AccountNotFoundException;
}

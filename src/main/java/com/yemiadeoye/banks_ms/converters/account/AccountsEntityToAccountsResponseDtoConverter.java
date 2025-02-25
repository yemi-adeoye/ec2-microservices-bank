package com.yemiadeoye.banks_ms.converters.account;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.yemiadeoye.banks_ms.dtos.response.AccountResponseDto;
import com.yemiadeoye.banks_ms.entities.AccountsEntity;

@Component
public class AccountsEntityToAccountsResponseDtoConverter implements Converter<AccountsEntity, AccountResponseDto> {

    @Override
    public AccountResponseDto convert(AccountsEntity accountEntity) {

        return new AccountResponseDto(
                accountEntity.getUserId(),
                accountEntity.getAccountBalance(),
                accountEntity.getEffectiveAccountBalance(),
                accountEntity.getAccountNumber(),
                accountEntity.getAccountType(),
                accountEntity.getCreatedAt());
    }

}

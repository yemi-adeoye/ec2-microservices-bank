package com.yemiadeoye.banks_ms.converters.account;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.yemiadeoye.banks_ms.dtos.request.AccountRequestDto;
import com.yemiadeoye.banks_ms.entities.AccountsEntity;

@Component
public class AccountRequestDtoToAccountEntityConverter implements Converter<AccountRequestDto, AccountsEntity> {

    @Override
    public AccountsEntity convert(AccountRequestDto accountRequestDto) {
        AccountsEntity account = new AccountsEntity();
        account.setUserId(accountRequestDto.userId());
        account.setAccountBalance(new BigDecimal(1000000L));
        account.setEffectiveAccountBalance(new BigDecimal(1000000L));
        account.setCreatedBy(accountRequestDto.createdBy());
        account.setAccountType(accountRequestDto.accountType());
        return account;
    }

}

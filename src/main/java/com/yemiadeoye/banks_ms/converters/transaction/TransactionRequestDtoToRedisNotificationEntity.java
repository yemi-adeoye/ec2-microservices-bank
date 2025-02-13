package com.yemiadeoye.banks_ms.converters.transaction;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.yemiadeoye.banks_ms.dtos.TransactionRequestDto;
import com.yemiadeoye.banks_ms.entities.RedisTransactionEntity;

@Component
public class TransactionRequestDtoToRedisNotificationEntity
        implements Converter<TransactionRequestDto, RedisTransactionEntity> {

    @Override
    public RedisTransactionEntity convert(TransactionRequestDto transactionRequestDto) {
        return RedisTransactionEntity
                .builder()
                .initiator(transactionRequestDto.initiator())
                .amount(transactionRequestDto.amount())
                .beneficiary(transactionRequestDto.beneficiary())
                .transactionDescription(transactionRequestDto.transactionDescription())
                .transactedBy(transactionRequestDto.transactedBy())
                .initiatorAccount(transactionRequestDto.initiatorAccount())
                .beneficiaryAccount(transactionRequestDto.beneficiaryAccount())
                .build();
    }

}

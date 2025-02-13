package com.yemiadeoye.banks_ms.converters.transaction;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.yemiadeoye.banks_ms.dtos.TransactionRequestDto;
import com.yemiadeoye.banks_ms.entities.TransactionsEntity;

@Component
public class TransactionRequestDtoToTransactionEntityConverter
        implements Converter<TransactionRequestDto, TransactionsEntity> {

    @Override
    public TransactionsEntity convert(TransactionRequestDto transactionRequestDto) {
        return TransactionsEntity
                .builder()
                .amount(transactionRequestDto.amount())
                .beneficiary(transactionRequestDto.beneficiary())
                .beneficiaryAccount(transactionRequestDto.beneficiaryAccount())
                .initiator(transactionRequestDto.initiator())
                .transactionDescription(transactionRequestDto.transactionDescription())
                .initiatorAccount(transactionRequestDto.initiatorAccount())
                .transactedBy(transactionRequestDto.transactedBy())
                .comment(transactionRequestDto.comment())
                .build();
    }

}

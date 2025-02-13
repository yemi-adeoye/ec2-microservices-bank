package com.yemiadeoye.banks_ms.converters.transaction;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.yemiadeoye.banks_ms.entities.RedisTransactionEntity;
import com.yemiadeoye.banks_ms.entities.TransactionsEntity;

@Component
public class RedisTransactionEntityToTransactionEntity
        implements Converter<RedisTransactionEntity, TransactionsEntity> {

    @Override
    public TransactionsEntity convert(RedisTransactionEntity redisTransactionEntity) {
        return TransactionsEntity.builder()
                .id(redisTransactionEntity.getId())
                .initiator(redisTransactionEntity.getInitiator())
                .amount(redisTransactionEntity.getAmount())
                .beneficiary(redisTransactionEntity.getBeneficiary())
                .transactionDescription(redisTransactionEntity.getTransactionDescription())
                .transactedBy(redisTransactionEntity.getTransactedBy())
                .initiatorAccount(redisTransactionEntity.getInitiatorAccount())
                .beneficiaryAccount(redisTransactionEntity.getBeneficiaryAccount())
                .comment(redisTransactionEntity.getComment())
                .build();
    }

}

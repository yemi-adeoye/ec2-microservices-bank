package com.yemiadeoye.banks_ms.entities;

import java.math.BigDecimal;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RedisHash
@Component
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RedisTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String initiator;
    private BigDecimal amount;
    private String beneficiary;
    private String transactionDescription;
    private String transactedBy;
    private String initiatorAccount;
    private String beneficiaryAccount;
    private String comment;
}

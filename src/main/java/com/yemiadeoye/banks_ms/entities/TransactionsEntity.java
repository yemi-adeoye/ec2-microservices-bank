package com.yemiadeoye.banks_ms.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "transactions")
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "initiator")
    private String initiator;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transacted_at")
    private Timestamp transactedAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "completed_at")
    private Timestamp completedAt;

    private String transactionStatus;

    private String transactionType;

    private String transactionDescription;

    private String beneficiaryAccount;

    private String initiatorAccount;

    private String transactedBy;

    @Column(name = "comment")
    private String comment;

    @Column(name = "notes")
    private String notes;
}

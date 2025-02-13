package com.yemiadeoye.banks_ms.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity(name = "accounts")
@Data
@ToString
public class AccountsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Column(name = "effective_account_balance")
    private BigDecimal effectiveAccountBalance;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    private String accountType;

    @Column(name = "is_not_active")
    private boolean isNotActive = true;

    @Column(name = "is_not_deleted")
    private boolean isNotDeleted = true;
}

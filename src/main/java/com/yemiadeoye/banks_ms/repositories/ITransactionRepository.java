package com.yemiadeoye.banks_ms.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yemiadeoye.banks_ms.entities.TransactionsEntity;

public interface ITransactionRepository extends JpaRepository<TransactionsEntity, String> {
    Page<TransactionsEntity> findAllByBeneficiaryAccount(String beneficiaryAccount, PageRequest pageRequest);
}

package com.yemiadeoye.banks_ms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yemiadeoye.banks_ms.entities.AccountsEntity;

import jakarta.transaction.Transactional;

public interface IAccountRepository extends JpaRepository<AccountsEntity, String> {

    public boolean existsByAccountNumber(String accountNumber);

    public AccountsEntity findByAccountNumber(String accountId);

    public List<AccountsEntity> findAllByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE accounts u SET u.isNotDeleted=false WHERE u.userId = :userId", nativeQuery = false)
    void deleteAllByUserId(@Param("userId") String userId);

}

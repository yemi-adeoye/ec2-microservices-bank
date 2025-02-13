package com.yemiadeoye.banks_ms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yemiadeoye.banks_ms.entities.RedisTransactionEntity;

@Repository
public interface IRedisTransactionRepository extends CrudRepository<RedisTransactionEntity, String> {

}

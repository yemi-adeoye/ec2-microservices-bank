package com.yemiadeoye.banks_ms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yemiadeoye.banks_ms.entities.NotificationEntity;

@Repository
public interface INotificationRepository extends CrudRepository<NotificationEntity, String> {
    List<NotificationEntity> findByUserIdAndIsRead(String UserId, boolean isRead);
}

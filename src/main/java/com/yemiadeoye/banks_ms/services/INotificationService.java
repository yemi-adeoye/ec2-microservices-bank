package com.yemiadeoye.banks_ms.services;

import java.util.List;

import com.yemiadeoye.banks_ms.dtos.response.NotificationUpdateResponseDto;
import com.yemiadeoye.banks_ms.entities.NotificationEntity;

public interface INotificationService {
    List<NotificationEntity> getUserNotifications(String userId);

    void sendNotification(String channel, String message);

    public NotificationUpdateResponseDto markNotificationAsRead(String notificationId);

}
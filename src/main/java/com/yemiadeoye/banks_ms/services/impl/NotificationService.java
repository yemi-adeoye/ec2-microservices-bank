package com.yemiadeoye.banks_ms.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yemiadeoye.banks_ms.dtos.response.NotificationUpdateResponseDto;
import com.yemiadeoye.banks_ms.entities.NotificationEntity;
import com.yemiadeoye.banks_ms.entities.TransactionsEntity;
import com.yemiadeoye.banks_ms.repositories.INotificationRepository;
import com.yemiadeoye.banks_ms.services.INotificationService;

import io.socket.client.Socket;

@Service
public class NotificationService implements INotificationService {

    private Socket socket;
    private INotificationRepository notificationRepository;

    public NotificationService(Socket socket, INotificationRepository notificationRepository) {
        this.socket = socket;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<NotificationEntity> getUserNotifications(String userId) {

        return notificationRepository.findByUserId(userId);
    }

    public NotificationEntity saveNotification(NotificationEntity notificationEntity) {

        return this.notificationRepository.save(notificationEntity);
    }

    @Override
    public void sendNotification(String channel, String message) {
        this.socket.emit(channel, message);
    }

    public String getNotificationMessage(TransactionsEntity transactionsEntity) {

        Map<String, String> notificationMap = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        String description = " withdrawn. ";

        if (transactionsEntity.getTransactionType().toUpperCase() == "DEPOSIT") {
            description = " deposited. ";
        }

        sb.append(transactionsEntity.getAmount());
        sb.append(description);
        sb.append("Account: " + transactionsEntity.getBeneficiaryAccount());
        sb.append("Transaction Status: " + transactionsEntity.getTransactionStatus());

        notificationMap.put("for", "yemi");
        notificationMap.put("notification", sb.toString());

        String message = "";
        try {
            message = new ObjectMapper().writeValueAsString(notificationMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public NotificationUpdateResponseDto markNotificationAsRead(String notificationId) {
        boolean isSuccessful = false;

        // get the notification
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(notificationId);

        // update the notification
        if (notificationEntity.isPresent()) {
            notificationEntity.get().setRead(true);
            try {
                notificationRepository.save(notificationEntity.get());
                isSuccessful = true;
            } catch (Exception e) {
                isSuccessful = false;
            }
        }

        return new NotificationUpdateResponseDto(notificationEntity.get().getId(), isSuccessful);
    }
}

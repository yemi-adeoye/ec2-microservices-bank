package com.yemiadeoye.banks_ms.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yemiadeoye.banks_ms.dtos.response.NotificationUpdateResponseDto;
import com.yemiadeoye.banks_ms.entities.NotificationEntity;
import com.yemiadeoye.banks_ms.services.INotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    private INotificationService notificationService;

    public NotificationsController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationEntity>> getNotificationsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok().body(notificationService.getUserNotifications(userId));
    }

    @PostMapping("/{notificationId}")
    public ResponseEntity<NotificationUpdateResponseDto> markRead(@PathVariable String notificationId) {
        return ResponseEntity.ok().body(notificationService.markNotificationAsRead(notificationId));
    }

}

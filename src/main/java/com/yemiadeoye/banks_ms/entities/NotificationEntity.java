package com.yemiadeoye.banks_ms.entities;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
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
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private boolean isRead;
    private String message;
}

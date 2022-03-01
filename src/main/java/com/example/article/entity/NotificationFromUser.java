package com.example.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class NotificationFromUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private UUID articleId;
    private UUID userId;

    private  boolean read;

    private  String notificationName;


    private  UUID administratorId;


    public NotificationFromUser(UUID articleId, UUID userId, boolean read, String notificationName) {
        this.articleId = articleId;
        this.userId = userId;
        this.read = read;
        this.notificationName = notificationName;
    }

    public NotificationFromUser(UUID articleId, UUID userId, boolean read, String notificationName, UUID administratorId) {
        this.articleId = articleId;
        this.userId = userId;
        this.read = read;
        this.notificationName = notificationName;
        this.administratorId = administratorId;
    }
}

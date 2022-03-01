package com.example.article.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

public class Firebase {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(unique = true)
    private String firebaseKey;

    private String info;

    private boolean active;

    @ManyToOne
    @JsonIgnore
    private User user;

    public Firebase(String firebaseKey,User user) {
        this.firebaseKey = firebaseKey;
        this.user        = user;
    }
}


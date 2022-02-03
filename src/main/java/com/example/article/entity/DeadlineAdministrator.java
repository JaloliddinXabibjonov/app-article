package com.example.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;
import java.util.zip.CheckedOutputStream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeadlineAdministrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private UUID administratorId;

    private Integer deadline;


}

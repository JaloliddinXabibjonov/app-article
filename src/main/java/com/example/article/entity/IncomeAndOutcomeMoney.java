package com.example.article.entity;

import com.example.article.entity.template.AbsEntity;
import com.example.article.entity.enums.PayStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class IncomeAndOutcomeMoney extends AbsEntity {

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;        //statusi

    private double summa;       //summasi

    private UUID articleId;     //qaysi maqola uchun to`lanayotganligi


}

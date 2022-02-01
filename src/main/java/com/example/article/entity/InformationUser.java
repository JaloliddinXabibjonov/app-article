package com.example.article.entity;

import com.example.article.entity.enums.UserStatus;
import com.example.article.entity.enums.Watdou;
import com.example.article.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InformationUser extends AbsEntity {


    @ManyToOne
    private User acceptedUser;


    @ManyToOne
    private User redactorAndReviewer;  // qaysi redactor yo Reviewer larni   qoshdi yoki o'chirdi


    private Date whenAndWho;   // qachon o'zgartirdi


    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;  // qaysi toifaga ozgartirdi

    public InformationUser(User acceptedUser, User redactorAndReviewer, Date whenAndWho) {
        this.acceptedUser = acceptedUser;
        this.redactorAndReviewer = redactorAndReviewer;
        this.whenAndWho = whenAndWho;
    }


}

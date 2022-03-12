package com.example.article.entity;

import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.Watdou;
import com.example.article.entity.template.AbsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InformationArticle extends AbsEntity {  //bu articldi kim va qachon

    // activ yoki qabul qilgan tebl userlarni qachon articlga murojat qilganini saqlab boradi

    // activ yoki qabul qilgan tebl


//    long deadline=10000L;

    @ManyToOne
    private User chekUser;  // qaysi user ozgartirdi

    @ManyToOne(fetch = FetchType.EAGER)
    private Article article;  // qaysi articildi

    @ManyToOne
    private User redactor;  // qaysi redactor yo Reviewer larni   qoshdi yoki o'chirdi

    private Date whenAndWho;   // qachon o'zgartirdi'

    @Enumerated(EnumType.STRING)
    private Watdou watdou;  // qaysi toifaga ozgartirdi


    @Enumerated(EnumType.STRING)
    private ArticleStatusName articleStatusName;   //redaktor qaysi statusni berdi redactorlar tomondan bertiladigon statuslar

    @JsonIgnore
    private String massage;

    private String description;
    private long deadline;

    @OneToOne
    private Attachment attachFile;

    public InformationArticle(User user, Article article, Date date, String description) {
        this.chekUser = user;
        this.article = article;
        this.whenAndWho = date;
        this.description = description;
    }

    public InformationArticle(User user, Article articleId, Date date, ArticleStatusName articleStatus, Attachment attachFile) {
        this.redactor = user;
        this.article = articleId;
        this.articleStatusName = articleStatus;
        this.whenAndWho = date;
        this.attachFile = attachFile;
    }

    public InformationArticle(User user, String description, Article articleId, Date date, ArticleStatusName articleStatus, Attachment attachFile) {
        this.redactor = user;
        this.description = description;
        this.article = articleId;
        this.articleStatusName = articleStatus;
        this.whenAndWho = date;
        this.attachFile = attachFile;
    }

    public InformationArticle(User user, Article articleId, Date date, ArticleStatusName articleStatus) {
        this.redactor = user;
        this.article = articleId;
        this.whenAndWho = date;
        this.articleStatusName = articleStatus;

    }

    public InformationArticle(User user, User redactors, Article byId, Date date, Watdou watdou, ArticleStatusName statusName) {
        this.chekUser = user;
        this.redactor = redactors;
        this.article = byId;
        this.watdou = watdou;
        this.whenAndWho = date;
        this.articleStatusName = statusName;
    }

    public InformationArticle(User user, Article byId, Date date, Watdou watdou, ArticleStatusName aNull, String massage) {
        this.chekUser = user;
        this.article = byId;
        this.whenAndWho = date;
        this.watdou = watdou;
        this.massage = massage;
        this.articleStatusName = aNull;
    }

    public InformationArticle(User chekUser, Article article, Date whenAndWho, ArticleStatusName articleStatusName, String massage) {
        this.redactor = chekUser;
        this.article = article;
        this.whenAndWho = whenAndWho;
        this.articleStatusName = articleStatusName;
        this.massage = massage;
    }

    public InformationArticle(User chekUser, Article article, Date whenAndWho, ArticleStatusName articleStatusName, String massage, Attachment attachFile, String description) {
        this.chekUser = chekUser;
        this.article = article;
        this.whenAndWho = whenAndWho;
        this.articleStatusName = articleStatusName;
        this.massage = massage;
        this.attachFile = attachFile;
        this.description = description;
    }

}

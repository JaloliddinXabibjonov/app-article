package com.example.article.entity;

import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.template.AbsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends AbsEntity {

    @Column(nullable = false)
    private String description;

    @OneToOne
    private PricesOfArticle price;

    @ManyToMany
    private Set<Authors> authors;


    private boolean deleted;

    private String titleArticle;

    private boolean publicPrivate;

    private boolean active = false;//Admin  o'zi o'zgartiradi  va edit qilishga aloqasi yoq

    private boolean pay = false;  //pul tolasa ozgaradi  va edit qilishga aloqasi yoq

    private Integer views;  // bu articldi qancha odam kordi va yuklab oldi

    //    @JsonIgnore
    private boolean confirm = false; //admin qabul qilganini bildiradi keyin bu maqolami edit qilib bo'lmaydi

//    private boolean rejected; // qayta ishlashga berildi adminlarning tasdiqlashi uchun





    @Enumerated(EnumType.STRING)
    private ArticleStatusName articleStatusName;


    @ManyToOne
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    private Attachment file;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;



    @ManyToMany
    private List<Journals> journals;

    private boolean journalsActive = false;

    @OneToOne
    private Attachment publishedArticle;

    @OneToOne
    private Attachment certificate;

    private String language;

    @OneToMany
    private  List<ArticleEditArxiv>articleEditArxiv;

    public Article(Category category, String title, boolean pay, String description) {
        this.category = category;
        this.titleArticle = title;
        this.pay = pay;
        this.description = description;

    }
}

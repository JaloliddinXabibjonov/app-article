package com.example.article.entity;

import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.template.AbsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends AbsEntity {

    @Column(nullable = false)
    private String description;

    @ManyToMany
    private Set<Authors> authors;

    private String titleArticle;

    private long price;

    private  boolean publicAndPrivate;

    private boolean active = false;//Admin  o'zi o'zgartiradi  va edit qilishga aloqasi yoq

    private boolean pay = false;  //pul tolasa ozgaradi  va edit qilishga aloqasi yoq

    private Integer views;  // bu articldi qancha odam kordi va yuklab oldi

    //    @JsonIgnore
    private boolean confirm = false; //admin qabul qilganini bildiradi keyin bu maqolami edit qilib bo'lmaydi

//    private boolean rejected; // qayta ishlashga berildi adminlarning tasdiqlashi uchun


    public Article(Category category, String title, boolean pay,String description) {
        this.category = category;
        this.titleArticle=title;
        this.pay=pay;
        this.description=description;

    }


    @Enumerated(EnumType.STRING)
    private ArticleStatusName articleStatusName;


    @ManyToOne
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    private Attachment file;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;


}

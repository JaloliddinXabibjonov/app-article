package com.example.article.entity;

import com.example.article.entity.enums.JournalsStatus;
import com.example.article.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Journals extends AbsEntity {

    @Column(nullable = false)
    private String name;


    private Date publishedDate;  // chop etilgan sana
    private Date receivedDate;  // maqolani qabul qilish sanasi

    @Enumerated(EnumType.STRING)
    private JournalsStatus journalsStatus; // maqola chop etilgan yo etilmaganini bildiradi



//    private Date receivedDate;  // maqolani qabul qilish sanasi

    private String articleReviewers; // bu maqolani kimlar korib chiqish haqida ma'lumot bunda hamma reviwerlar haqida malumot ilmiy kengash haqida

    private String maqolaJurnaldaNechaKundaChiqishi;
    private String jurnalNechaKundaChiqishi;
    private String ISSN;
    private String jurnalSertificat;


    @ManyToMany
    private List<Category> category;

    @OneToOne
    private Attachment photoJournals;

    @ManyToMany
    private Set<Article> articles;

    @OneToOne
    private Attachment file;



}
package com.example.article.entity;

import com.example.article.entity.enums.JournalsStatus;
import com.example.article.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Journals extends AbsEntity {

    @Column(nullable = false)
    private String name;

    private int numberOfThisYear;       /**BU YILGI SONI*/
    private int generalNumber;          /**UMUMIY SONI*/

    private Date receivedDate;  // maqolani qabul qilish sanasi

    @Enumerated(EnumType.STRING)
    private JournalsStatus journalsStatus; // maqola chop etilgan yo etilmaganini bildiradi

    private UUID parentId;              /**QAYSI JURNALNING SONI*/

    private String articleReviewers; // bu maqolani kimlar korib chiqish haqida ma'lumot bunda hamma reviwerlar haqida malumot ilmiy kengash haqida

    private int printedDate;        /**NECHA KUNDA NASHR ETILISHI*/
    private String ISSN;
    private String ISBN;
    private String certificateOfJournals;

    @ManyToOne
    private Category category;

    @OneToOne
    private Attachment photo;

    @ManyToMany
    private Set<Article> articles;

    @OneToOne
    private Attachment file;

    private boolean deleted;
}
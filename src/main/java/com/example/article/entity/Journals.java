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

    private String createdDate;
    @Column(nullable = false)
    private String title;

    private int releaseNumberOfThisYear;       /**BU YILGI SONI*/
    private int allReleasesNumber;          /**UMUMIY SONI*/

    private Date deadline;  // maqolani qabul qilish sanasi

//    @Enumerated(EnumType.STRING)
    private String journalsStatus; // maqola chop etilgan yo etilmaganini bildiradi

    private UUID parentId;              /**QAYSI JURNALNING SONI*/

    private String description; // bu maqolani kimlar korib chiqish haqida ma'lumot bunda hamma reviwerlar haqida malumot ilmiy kengash haqida

    private int printedDate;        /**NECHA KUNDA NASHR ETILISHI*/
    private String ISSN;
    private String ISBN;
    private String certificateNumber;

    @ManyToOne
    private Category category;

    @OneToOne
    private Attachment cover;

    @ManyToMany
    private Set<Article> articles;

    @OneToOne
    private Attachment file;

    private boolean deleted;
}
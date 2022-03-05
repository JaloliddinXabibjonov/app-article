package com.example.article.payload;

import com.example.article.entity.Article;
import com.example.article.entity.Attachment;
import com.example.article.entity.Category;
import com.example.article.entity.enums.JournalsStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalsPayload {

    private UUID id;
    private String title;
    private int releaseNumberOfThisYear;
    private String createdDate;
    private String status;
    /**
     * BU YILGI SONI
     */
    private int allReleasesNumber;
    /**
     * UMUMIY SONI
     */
    private String deadline;  // maqolani qabul qilish sanasi
    private UUID parentId;
    /**
     * QAYSI JURNALNING SONI
     */
    private String description; // bu maqolani kimlar korib chiqish haqida ma'lumot bunda hamma reviwerlar haqida malumot ilmiy kengash haqida
    private int printedDate;
    /**
     * NECHA KUNDA NASHR ETILISHI
     */
    private String issn;
    private String isbn;
    private String certificateNumber;
    private int categoryId;

    private Set<Article> articles;
}

package com.example.article.payload;

import com.example.article.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalsPayload {
 private  UUID id;
    private String name;
    private Date publishedDate;  // chop etilgan sana
    private Date receivedDate;  // maqolani qabul qilish sanasi

    private String articleReviewers; // bu maqolani kimlar korib chiqish haqida ma'lumot bunda hamma reviwerlar haqida malumot ilmiy kengash haqida

    private String maqolaJurnaldaNechaKundaChiqishi;
    private String jurnalNechaKundaChiqishi;
    private String ISSN;
    private String jurnalSertificat;

    private Integer   categoryId;

}

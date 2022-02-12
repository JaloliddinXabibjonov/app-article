package com.example.article.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForDashboard {
    private Integer numberOfAdmins;                              //MAQOLA BERUVCHILAR SONI
    private Integer numberOfUsers;                              //MAQOLA BERUVCHILAR SONI
    private Integer numberOfReviewers;                          //REVIEWERLAR SONI
    private Integer numberOfRedactors;                          //REDACTORLAR SONI

    private Integer numberOfNewAndPayFalse;                     //hali puli to`lanmagan maqolalar
    private Integer numberOfNewArticles;                        //yangi puli to`langan maqolalar soni
    private Integer numberOfIsBeingEditedArticles;              //tahrirlanayotgan maqolalar soni
    private Integer numberOfInReviewArticles;                   //nashrga tayyorlanayotgan maqolalar soni
    private Integer numberOfFreeAndPublishedArticles;           //nashr qilingan bepul maqolalar soni
    private Integer numberOfReadyOfPublicationArticles;         //nashrga tayyor maqolalar soni
    private Integer numberOfPaidAndPublishedArticles;           //nashr qilingan va pullik maqolalar soni
    private Integer numberOfRecycleArticles;                    //QAYTA ISHLASHGA YUBORILGAN MAQOLALAR SONI
    private Integer numberOfRejectedArticles;                    //BEKOR QILINGAN MAQOLALAR SONI

}

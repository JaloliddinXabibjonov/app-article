package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForDashboard {

    private Integer numberOfUsers;                              //MAQOLA BERUVCHILAR SONI
    private Integer numberOfReviewers;                          //REVIEWERLAR SONI
    private Integer numberOfRedactors;                          //REDACTORLAR SONI
    private Integer numberOfNewArticles;                        //yangi maqolalar soni
    private Integer numberOfIsBeingEditedArticles;              //tahrirlanayotgan maqolalar soni
    private Integer numberOfInReviewArticles;                   //nashrga tayyorlanayotgan maqolalar soni
    private Integer numberOfPublishedArticles;                  //nashrga tayyorlanayotgan maqolalar soni
    private Integer numberOfReadyOfPublicationArticles;         //nashrga tayyor maqolalar soni
    private Integer numberOfPaidAndPublishedArticles;           //nashr qilingan va pullik maqolalar soni

}

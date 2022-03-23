package com.example.article.payload;

import com.example.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyTasksDto {
    private Article article;
    private Integer numberOfReviewers;
    private String sendDate;
    private String deadLine;

//    public MyTasksDto(Article article, String sendDate, long deadLine) {
//        this.article = article;
//        this.sendDate = sendDate;
//        this.deadLine = deadLine;
//    }


    public MyTasksDto(Article article, String sendDate, String deadLine) {
        this.article = article;
        this.sendDate = sendDate;
        this.deadLine = deadLine;
    }
}

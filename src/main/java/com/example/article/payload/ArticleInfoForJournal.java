package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.UUID;

@Getter
@Setter
public class ArticleInfoForJournal {
    private UUID articleId;
    private String titleArticle;
    private UUID fileId;
    private String originalName;
    private String contentType;
    private int articleViews;


}

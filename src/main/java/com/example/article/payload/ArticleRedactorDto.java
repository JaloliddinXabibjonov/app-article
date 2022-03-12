package com.example.article.payload;

import com.example.article.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRedactorDto {

    private UUID articleId;
    private  String description;
    private Attachment publishedArticle;
}

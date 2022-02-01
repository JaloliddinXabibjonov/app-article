package com.example.article.payload;

import com.example.article.entity.Article;
import com.example.article.entity.InformationArticle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleInfoDto {
private Article article;
private InformationArticle informationArticle;

}

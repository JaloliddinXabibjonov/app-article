package com.example.article.payload;

import com.example.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleInfo {
    private Article article;

    private List<ArticleAdminInfo> articleAdminInfoList;





}

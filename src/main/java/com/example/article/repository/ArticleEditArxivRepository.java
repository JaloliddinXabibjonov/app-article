package com.example.article.repository;

import com.example.article.entity.ArticleEditArxiv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticleEditArxivRepository extends JpaRepository<ArticleEditArxiv, UUID> {
    ArticleEditArxiv findByArticleId(UUID articleId);

}

package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AddArticleDto {
    private UUID journalsId;
    private String description;
    private String author;
    private String titleArticle;
    private boolean publicPrivate;
    private Integer categoryId;
    private List<Integer> authorsList;
    private int sahifaSoni;
    private int jurnaldaChopEtishSoni;
    private int bosmaJurnalSoni;
    private int sertifikatSoni;
    private boolean doi;
    private double price;
    private int languageId;
}

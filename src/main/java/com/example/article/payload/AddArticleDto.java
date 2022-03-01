package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddArticleDto {
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
}

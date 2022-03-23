package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ArticleDto {

    private UUID    journalsId;
    private UUID    id;
    private String description;
    private List<Integer> authorsList;
    private String titleArticle;
    private boolean publicPrivate;
    private Integer categoryId;
    private UUID fileId;

    private boolean active = false;
    private boolean confirm = false;
    private Double price;

    /**
     * NARXNI HISOBLASH PARAMETRLARI
     */
    private Integer sahifaSoni;
    private Integer JurnaldaChopEtishSoni;
    private Integer BosmaJurnalSoni;
    private Integer SertifikatSoni;
    private boolean doi;
}

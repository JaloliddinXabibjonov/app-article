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

    private UUID id;
    private String description;
    private String author;
    private String titleArticle;
    private boolean publicPrivate;
    private Integer categoryId;
    private UUID fileId;

    private boolean active = false;
    private boolean confirm = false;
    private List<String> authorsList;

    /**
     * NARXNI HISOBLASH PARAMETRLARI
     */
    private int sahifaSoni;
    private int JurnaldaChopEtishSoni;
    private int BosmaJurnalSoni;
    private int SertifikatSoni;
    private boolean doi;
}

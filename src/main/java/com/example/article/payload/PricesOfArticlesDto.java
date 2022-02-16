package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class PricesOfArticlesDto {
    private Integer id;
    private double sahifaNarxi;
    private double bittaJurnaldaChopEtishNarxi;
    private double bittaBosmaJunalNarxi;
    private double bittaSertifikatNarxi;
    private double doi;
}

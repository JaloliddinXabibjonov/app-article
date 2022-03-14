package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ArticlePricesDto {
    private double sahifaNarxi;
    private double bittaBosmaJunalNarxi;
    private double bittaSertifikatNarxi;
    private double doi;
}

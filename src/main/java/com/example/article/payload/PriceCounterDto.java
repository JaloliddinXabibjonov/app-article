package com.example.article.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PriceCounterDto {

    private int sahifaSoni;
    private int jurnaldaChopEtishSoni;
    private int bosmaJurnalSoni;
    private int sertifikatSoni;
    private boolean doi;

    //    private double price;
}

package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceCounterDto {

    private int sahifaSoni;
    private int JurnaldaChopEtishSoni;
    private int BosmaJurnalSoni;
    private int SertifikatSoni;
    private boolean doi;
}

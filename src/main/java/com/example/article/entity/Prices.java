package com.example.article.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Prices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private double chopEtishNarxi;
    @Column(nullable = false)
    private double sahifaNarxi;
    @Column(nullable = false)
    private double bittaBosmaJunalNarxi;
    @Column(nullable = false)
    private double bittaSertifikatNarxi;
    @Column(nullable = false)
    private double doi;


}

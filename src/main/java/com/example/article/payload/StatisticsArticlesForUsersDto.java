package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsArticlesForUsersDto {
    private Integer accepteds;
    private Integer didNotAccepteds;
    private Integer checkAndAccepteds;
    private Integer checkAndCancels;
    private Integer checkAndRecycles;
}

package com.example.article.payload;

import com.example.article.entity.enums.ArticleStatusName;
import lombok.Data;

import java.util.UUID;

@Data
public class TimeBetween {
    private long start;
    private long end;
    private UUID userId;

}

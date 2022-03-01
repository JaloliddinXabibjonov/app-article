package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendSmsUserPriceDto {

    private UUID articleId;
    private  String text;
    private  String recipient;
    private  long  newPrice;
}

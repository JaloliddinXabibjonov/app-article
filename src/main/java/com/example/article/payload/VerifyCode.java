package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCode {
    private int code;
    private String password;
}

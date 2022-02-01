package com.example.article.payload;

import com.example.article.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User2 {
    private User user;
    private boolean confirm;
}
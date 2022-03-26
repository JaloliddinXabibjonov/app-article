package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalDirections {
    private String title;
    private String description;

    public JournalDirections(String title, String description) {
        this.title = title;
        this.description = description;
    }
}

package com.example.article.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttachArticleToJournal {
    private UUID articleId;
    private UUID journalId;
    private boolean action;
}

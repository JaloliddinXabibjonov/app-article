package com.example.article.payload;

import com.example.article.entity.Attachment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class ArticlesForReviewers {
    private Date processDate;
    private UUID id;
    private String title;
    private String printedJournalName;
    private String status;
    private UUID fileId;
    private Attachment file;
    private String originalName;
    private String contentType;
    private String description;
}

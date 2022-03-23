package com.example.article.payload;

import com.example.article.entity.Attachment;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class JournalInfo {

//    private Attachment file;

    private UUID journalId;
    private String contentType;
    private String originalName;

    private String publishedDate;
    private int releaseNumberOfThisYear;
    private int allReleaseNumber;
    private Attachment cover;
    private List<ArticleInfoForJournal> articleInfoForJournal;
}

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
    private Attachment file;

    private String publishedDate;
    private int releaseNumberOfThisYear;
    private int allReleaseNumber;

    private List<ArticleInfoForJournal> articleInfoForJournal;
}

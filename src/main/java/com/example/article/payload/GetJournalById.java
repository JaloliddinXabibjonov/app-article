package com.example.article.payload;

import com.example.article.entity.Article;
import com.example.article.entity.Journals;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class GetJournalById {
    private Journals journals;
    private String deadline;

    private List<Article> articles;

    private String publishedDate;
    private int releaseNumberOfThisYear;
    private int allReleaseNumber;

//    private  String articleTitle;
//    private  String articleStatus;
//    private  boolean publicPrivate;
}

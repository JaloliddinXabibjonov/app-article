package com.example.article.payload;

import com.example.article.entity.Attachment;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class ActiveJournalsDto {
    private UUID id;

    private String title;
    private Date deadline;
    private Attachment cover;
    private int year;
    private int releaseNumberOfThisYear;
    private int allReleaseNumber;
}

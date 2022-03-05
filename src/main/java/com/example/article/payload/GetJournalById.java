package com.example.article.payload;

import com.example.article.entity.Journals;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetJournalById {
    private Journals journals;
    private String deadline;
}

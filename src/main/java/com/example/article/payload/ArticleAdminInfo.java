package com.example.article.payload;

import com.example.article.entity.Attachment;
import com.example.article.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAdminInfo {
    private User admin;
    private String processDate;
    private String status;

    /**-------------------*/
    private String comment;
    private Attachment file;

    public ArticleAdminInfo(User admin, String processDate, String status) {
        this.admin = admin;
        this.processDate = processDate;
        this.status = status;
    }
}

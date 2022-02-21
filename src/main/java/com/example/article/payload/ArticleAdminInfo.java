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
    private String fullName;
    private String role;
    private String processDate;
    private String status;

    /**-------------------*/
    private String comment;
    private Attachment file;


}

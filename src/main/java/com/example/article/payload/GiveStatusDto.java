package com.example.article.payload;

import com.example.article.entity.enums.ArticleStatusName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiveStatusDto {
//    @NotNull(message = "Maqolani id si bo`sh bo`lmasligi kerak")
    private UUID articleId;
//    @NotNull(message = "Status bo`sh bo`lmasligi kerak")
    private ArticleStatusName status;
//    @NotNull(message = "Maqolani id si bo`sh bo`lmasligi kerak")
    private String description;

}

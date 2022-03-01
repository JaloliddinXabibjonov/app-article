package com.example.article.entity;

import com.example.article.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Authors extends AbsEntity {

    @OneToOne
    private User authorId;

    private Integer code;

}

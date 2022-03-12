package com.example.article.entity;

import com.example.article.entity.template.AbsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Authors extends AbsEntity {

    @JsonIgnore
    @OneToOne
    private User authorId;

    private String fullname;

    private Integer code;

}

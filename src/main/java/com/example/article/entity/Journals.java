package com.example.article.entity;

import com.example.article.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Journals extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany
    private List<Category> category;

    @OneToOne
    private Attachment photo;

    @ManyToMany
    private Set<Article> articles;

    @OneToOne
    private Attachment file;


}

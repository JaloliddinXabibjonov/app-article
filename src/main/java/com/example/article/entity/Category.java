package com.example.article.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    private boolean deleted;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent",cascade = CascadeType.ALL)
//    private List<Category> children;

    public Category( String name) {
        this.name = name;
    }


}

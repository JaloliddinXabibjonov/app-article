package com.example.article.entity;

import com.example.article.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attachment extends AbsEntity {
    private String originalName;

    private long size;

    private String contentType;

    private String fileName;

    private String path;

    public Attachment(String originalName, long size, String contentType, String fileName) {
        this.originalName = originalName;
        this.size = size;
        this.contentType = contentType;
        this.fileName = fileName;
    }

}

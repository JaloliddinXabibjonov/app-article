package com.example.article.repository;

import com.example.article.entity.Authors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorsRepository extends JpaRepository<Authors, UUID> {
}

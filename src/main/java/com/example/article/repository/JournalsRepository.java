package com.example.article.repository;

import com.example.article.entity.Journals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JournalsRepository extends JpaRepository<Journals, UUID> {



}

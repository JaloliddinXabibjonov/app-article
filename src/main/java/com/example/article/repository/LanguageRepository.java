package com.example.article.repository;

import com.example.article.entity.Languages;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Languages, Integer> {

    List<Languages> findAllByActiveTrueAndDeletedFalse();

    List<Languages> findAllByDeletedFalse();
    Optional<Languages> findByDeletedFalseAndId(Integer id);
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
//    Languages findByDeletedFalseAndId(Integer id);
}

package com.example.article.repository;

import com.example.article.entity.Journals;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JournalsRepository extends JpaRepository<Journals, UUID> {

    Optional<Journals> findByParentIdAndDeletedTrue(UUID parentId);

    Journals getByIdAndDeletedTrue(UUID id);

}

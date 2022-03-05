package com.example.article.repository;

import com.example.article.entity.Article;
import com.example.article.entity.Journals;
import com.example.article.entity.enums.JournalsStatus;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JournalsRepository extends JpaRepository<Journals, UUID> {

    Optional<Journals> findByParentIdAndDeletedTrue(UUID parentId);

    Journals getByIdAndDeletedTrue(UUID id);

    @Query(value = "select all_releases_number from journals  where parent_id=?1 or id=?1 order by created_at desc limit 1", nativeQuery = true)
    int findAllReleaseNumberByParentIdAndLastPublished(UUID parentId);

    List<Journals> findAllByDeletedTrueAndJournalsStatus(JournalsStatus journalsStatus);


    List<Journals> findAllByDeletedTrueAndParentIdNullAndCategoryId(Integer category_id);

    List<Journals>findAllByDeletedTrueAndParentId( UUID parentId);

    @Query(value = "select a.* from journals j inner join journals_articles ja on j.id = ja.journals_id and j.id=?1 inner join article a on ja.articles_id = a.id",nativeQuery = true)
    List<Article> findAllArticlesFromMagazine(UUID uuid);

    Optional<Journals> findByDeletedTrueAndId(UUID id);
}

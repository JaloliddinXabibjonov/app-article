package com.example.article.repository;

import com.example.article.entity.Journals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface JournalsRepository extends JpaRepository<Journals, UUID> {

    Optional<Journals> findByParentIdAndDeletedTrue(UUID parentId);

    Journals getByIdAndDeletedTrue(UUID id);

    @Query(value = "select all_releases_number from journals  where parent_id=?1 or id=?1 order by created_at desc limit 1", nativeQuery = true)
    int findAllReleaseNumberByParentIdAndLastPublished(UUID parentId);

    List<Journals> findAllByDeletedTrueAndJournalsStatus(String journalsStatus);


    List<Journals> findAllByDeletedTrueAndParentIdNullAndCategoryId(Integer category_id);
    List<Journals> findAllByDeletedTrueAndParentIdNull();

    List<Journals> findAllByDeletedTrueAndParentId(UUID parentId);

//    @Query(value = "select a.* from journals j inner join journals_articles ja on j.id = ja.journals_id and j.id=?1 inner join article a on ja.articles_id = a.id",nativeQuery = true)
//    List<Article> findAllArticlesFromMagazine(UUID uuid);

    Optional<Journals> findByDeletedTrueAndId(UUID id);


    Journals findByIdAndDeletedTrue(UUID id);



    List<Journals> findAllByDeletedTrueAndIdOrDeletedTrueAndId(UUID id, UUID id2);


    List<Journals> findAllByDeletedTrueAndIdAndParentIdOrDeletedTrueAndIdOrDeletedTrueAndParentId(UUID id, UUID parentId, UUID id2, UUID parentId2);


//    List<Journals> findAllByDeletedTrueAndActiveTrue

    List<Journals>findAllByDeletedTrueAndJournalsStatusAndId(String journalsStatus, UUID id);


    @Query(value = "select  date_publication from  journals where (deleted=true and id=?1) or (deleted=true and parent_id=?1) order by date_publication desc",nativeQuery = true)
   Set<Integer> findAllByDeletedTrueAndIdOrDeletedTrueAndParentId(UUID id, UUID parentId);

List<Journals> findAllByDeletedTrueAndDatePublicationAndIdOrDeletedTrueAndDatePublicationAndParentIdOrderByReleaseNumberOfThisYear(int datePublication, UUID id, int datePublication2, UUID parentId);
List<Journals> findAllByDeletedTrueAndDatePublicationAndIdOrDeletedTrueAndDatePublicationAndParentId(int datePublication, UUID id, int datePublication2, UUID parentId);



}

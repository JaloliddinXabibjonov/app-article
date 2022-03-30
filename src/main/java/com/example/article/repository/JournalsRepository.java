package com.example.article.repository;

import com.example.article.entity.Category;
import com.example.article.entity.Journals;
import com.example.article.payload.JournalDirections;
import com.example.article.payload.JournalsDir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface JournalsRepository extends JpaRepository<Journals, UUID> {

    Optional<Journals> findByParentIdAndDeletedTrue(UUID parentId);

    Journals getByIdAndDeletedTrue(UUID id);

    @Query(value = "select all_releases_number from journals  where parent_id=?1 or id=?1 order by created_at desc limit 1", nativeQuery = true)
    int findAllReleaseNumberByParentIdAndLastPublished(UUID parentId);

    @Query(value = "select release_number_of_this_year from journals where parent_id=?1 and date_publication=?2 and title=?3", nativeQuery = true)
    Optional<Integer> findByReleaseNumberOfThisYear(UUID parentId, int datePublication, String title);

    @Query(value = "select date_of_publication from journals  where id=?1 ", nativeQuery = true)
    Date findDatePublicationByIdAndDeletedTrue(UUID id);

    @Query(value = "select date_publication from journals  where parent_id=?1 and date_publication=?2 and title=?3 order by created_at desc limit 1", nativeQuery = true)
    Optional<Integer> findDatePublicationByParentIdAndDeletedTrueAndDatePublication(UUID uuid, int year , String title);

    int findReleaseNumberOfThisYearByParentId(UUID parentId);

    List<Journals> findAllByDeletedTrueAndJournalsStatus(String journalsStatus);


    List<Journals> findAllByDeletedTrueAndParentIdNullAndCategoryId(Integer category_id);

    List<Journals> findAllByDeletedTrueAndParentIdNullAndCategoryIdAndJournalsStatus(Integer category_id, String journalsStatus);

    List<Journals> findAllByDeletedTrueAndParentIdNull();
//    List<Journals> findAllByDeletedTrueAndParentIdNullAndJournalsStatus(String journalsStatus);

    List<Journals> findAllByDeletedTrueAndParentIdNullAndJournalsStatus(String journalsStatus);

    List<Journals> findAllByDeletedTrueAndParentId(UUID parentId);

    List<Journals> findAllByDeletedTrueAndParentIdAndJournalsStatus(UUID parentId, String status);


//    @Query(value = "select a.* from journals j inner join journals_articles ja on j.id = ja.journals_id and j.id=?1 inner join article a on ja.articles_id = a.id",nativeQuery = true)
//    List<Article> findAllArticlesFromMagazine(UUID uuid);

    Optional<Journals> findByDeletedTrueAndId(UUID id);


    Journals findByIdAndDeletedTrue(UUID id);

    Journals findByIdAndDeletedTrueAndJournalsStatus(UUID id, String journalStatus);


    List<Journals> findAllByDeletedTrueAndIdOrDeletedTrueAndId(UUID id, UUID id2);


    List<Journals> findAllByDeletedTrueAndIdAndParentIdOrDeletedTrueAndIdOrDeletedTrueAndParentId(UUID id, UUID parentId, UUID id2, UUID parentId2);


//    List<Journals> findAllByDeletedTrueAndActiveTrue

    List<Journals> findAllByDeletedTrueAndJournalsStatusAndId(String journalsStatus, UUID id);


    @Query(value = "select  date_publication from  journals where (deleted=true and id=?1) or (deleted=true and parent_id=?1) order by date_publication DESC ", nativeQuery = true)
    Set<Integer> findAllByDeletedTrueAndIdOrDeletedTrueAndParentId(UUID id, UUID parentId);

    @Query(value = "select  date_publication from  journals where (deleted=true and id=?1 and journals_status='PUBLISHED') or (deleted=true and parent_id=?1  and journals_status='PUBLISHED') order by date_publication DESC ", nativeQuery = true)
    Set<Integer> findAllByDeletedTrueAndIdAndPublishedOrDeletedTrueAndParentIdAndPublished(UUID id, UUID parentId);

    List<Journals> findAllByDeletedTrueAndDatePublicationAndIdOrDeletedTrueAndDatePublicationAndParentIdOrderByReleaseNumberOfThisYear(int datePublication, UUID id, int datePublication2, UUID parentId);

    List<Journals> findAllByDeletedTrueAndDatePublicationAndIdOrDeletedTrueAndDatePublicationAndParentId(int datePublication, UUID id, int datePublication2, UUID parentId);

    List<Journals> findAllByDeletedTrueAndDatePublicationAndIdAndJournalsStatusOrDeletedTrueAndDatePublicationAndParentIdAndJournalsStatus(int datePublication, UUID id, String journalsStatus, int datePublication2, UUID parentId, String journalsStatus2);

    @Query(value = "select title, description from journals where journals_status='PUBLISHED' AND parent_id is null", nativeQuery = true)
    Set<JournalsDir> findAllByJournalsStatusAndParentIdIsNull();


}

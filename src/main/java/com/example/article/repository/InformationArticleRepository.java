package com.example.article.repository;

import com.example.article.entity.Article;
import com.example.article.entity.InformationArticle;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.Watdou;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InformationArticleRepository extends JpaRepository<InformationArticle, UUID> {

    Optional<InformationArticle> findFirstByArticleIdOrderByCreatedAtDesc(UUID article_id);

    boolean existsByArticleIdAndChekUserIdAndArticleStatusName(UUID article_id, UUID chekUser_id, ArticleStatusName articleStatusName);

    InformationArticle findFirstByArticleIdAndChekUserIdOrderByCreatedAtAsc(UUID articleId, UUID uuid);

    @Query(value = "select * from information_article where article_id=?1", nativeQuery = true)
    List<InformationArticle> findAllByArticleId(UUID article_id);

    InformationArticle findByArticleId(UUID article_id);

//    InformationArticle findByArticleIdAndRedactorReviewerArticleStatusIdAndArticleStatusName(UUID article_id, UUID redactorReviewerArticleStatus_id, ArticleStatusName articleStatusName);

    boolean existsByArticleIdAndRedactorIdAndArticleStatusName(UUID article_id, UUID redactor_id, ArticleStatusName articleStatusName);

    boolean existsByArticleIdAndArticleStatusName(UUID article_id, ArticleStatusName articleStatusName);

    List<InformationArticle> findAllByArticleIdAndRedactorIdAndWatdou(UUID article_id, UUID redactor_id, Watdou watdou);

    InformationArticle findByArticleIdAndRedactorIdAndArticleStatusName(UUID article_id, UUID redactor_id, ArticleStatusName articleStatusName);

//    boolean existsByArticleIdAndArticleStatusNameAndRedactorIdOrArticleIdAndArticleStatusNameAndRedactorIdOrArticleIdAndArticleStatusNameAndRedactorId(UUID article_id, ArticleStatusName articleStatusName, UUID redactor_id, UUID article_id2, ArticleStatusName articleStatusName2, UUID redactor_id2, UUID article_id3, ArticleStatusName articleStatusName3, UUID redactor_id3);

    List<InformationArticle> findAllByArticleIdAndArticleStatusName(UUID article_id, ArticleStatusName articleStatusName);

    List<InformationArticle> findAllByArticleIdAndMassageNotNullOrderByCreatedAt(UUID articleId);

    @Query(value = "select redactor_id   from information_article   where article_id=?1 and article_status_name=?2 ", nativeQuery = true)
    List<UUID> findAllByArticleIds(UUID articleId, String articleStatusName);


    @Query(value = "select created_at from  information_article where article_id=?1 and redactor_id=?2 and article_status_name=?3", nativeQuery = true)
    Timestamp findByReturnCreateAt(UUID articleId, UUID redactorId, ArticleStatusName articleStatusName);


//    @Query(value = "select * from editors_article e\n" +
//            "             inner join role r on r.role_name = ?2\n" +
//            "             inner join users_roles ur on ur.roles_id=r.id\n" +
//            "             inner join users_roles ur2 on ur.users_id=e.redactor_id\n" +
//            "            where e.article_id=?1",nativeQuery = true)
//    List<EditorsArticle> findAllByArticleIdAndRoleName(UUID articleId, String roleName);


    @Query(value = "select  * from  information_article ia\n" +
            "    inner join editors_article ea  on ea.article_id=?1 and  ea.role_id=?2 and ia.redactor_id=ea.redactor_id\n" +
            "and  ia.article_status_name='I_ACCEPTED'   ", nativeQuery = true)
    List<InformationArticle> findAllByRedactorRol(UUID articleId, Integer roleId);

    List<InformationArticle> findAllByArticleIdAndRedactorIdAndArticleStatusNameOrArticleIdAndRedactorIdAndArticleStatusNameOrArticleIdAndRedactorIdAndArticleStatusName(UUID article_id, UUID redactor_id, ArticleStatusName articleStatusName, UUID article_id2, UUID redactor_id2, ArticleStatusName articleStatusName2, UUID article_id3, UUID redactor_id3, ArticleStatusName articleStatusName3);

    List<InformationArticle> findAllByArticleStatusName(ArticleStatusName statusName);

    List<InformationArticle> findAllByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);

    List<InformationArticle> findAllByRedactorIdAndCreatedAtIsBetweenOrderByCreatedAtDesc(UUID redactor_id, Timestamp createdAt, Timestamp createdAt2);

    List<InformationArticle> findAllByChekUserIdAndWatdou(UUID chekUser_id, Watdou watdou);

    @Query(value = "select ia.article_id from information_article ia where ia.article_id = ?1 and ia.article_status_name=?2", nativeQuery = true)
    List<UUID> findAllByArticleIdSAndArticleStatusName(UUID articleId, String statusName);


    @Query(value = "select * from information_article  where article_id in ?1 and article_status_name = ?2 order by created_at desc", nativeQuery = true)
    List<InformationArticle> findAllByArticleIdAndArticleStatusNameOrderByCreatedAtDesc(List<UUID> article, String articleStatusName);

    List<InformationArticle> findAllByRedactorIdAndArticleStatusName(UUID redactor_id, ArticleStatusName articleStatusName);

    boolean existsByArticleIdAndArticleStatusNameAndRedactorId(UUID article_id, ArticleStatusName articleStatusName, UUID redactor_id);

    boolean existsByArticleIdAndRedactorIdAndArticleStatusNameOrArticleIdAndRedactorIdAndArticleStatusNameOrArticleIdAndRedactorIdAndArticleStatusName(UUID article_id, UUID redactor_id, ArticleStatusName articleStatusName, UUID article_id2, UUID redactor_id2, ArticleStatusName articleStatusName2, UUID article_id3, UUID redactor_id3, ArticleStatusName articleStatusName3);

    @Query(value = "select a.* from article a inner join information_article ia on a.id = ia.article_id and ia.article_status_name=:status and ia.redactor_id=:redactorId where ia.created_at between :start and :end", nativeQuery = true)
    List<Article> findAllByCreatedAtBetweenAndArticleStatusNameAndRedactorId(Timestamp start, Timestamp end, ArticleStatusName status, UUID redactorId);

    Integer countAllByArticleStatusNameAndRedactorId(ArticleStatusName articleStatusName, UUID redactor_id);

    List<InformationArticle> findAllByChekUserIdAndArticleId(UUID chekUser_id, UUID article_id);

//    List<InformationArticle> findAllByRedactorIdAndArticleId(UUID redactor_id, UUID article_id);

    List<InformationArticle> findAllByArticleStatusNameAndIdOrArticleStatusNameAndIdOrArticleStatusNameAndId(ArticleStatusName articleStatusName, UUID id, ArticleStatusName articleStatusName2, UUID id2, ArticleStatusName articleStatusName3, UUID id3);


    InformationArticle findByRedactorId(UUID redactor_id);

}

package com.example.article.repository;

import com.example.article.entity.Article;
import com.example.article.entity.Authors;
import com.example.article.entity.Journals;
import com.example.article.entity.enums.ArticleStatusName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    List<Article> findAllByCreatedBy(UUID id);

    List<Article> findAllByIdIn(Collection<UUID> id);

    Page<Article> findByPayTrue(Pageable simplePageable);


//    Page<Article>findAllByChecked(String checked, Pageable pageable);

    Page<Article> findByActiveTrue(Pageable simplePageable);




    List<Article> findAllByPayTrueAndConfirmFalse();


    Article findByConfirmTrueAndId(UUID id);


    @Query(value = "select  * from  article a  inner join information_article ia on ia.watdou='CONFIRM' and ia.chek_user_id=?1 and a.id=ia.article_id  and a.article_status_name=?2  and a.active=true", nativeQuery = true)
    List<Article> findAllByConfirmTrueAndAdmin(UUID id, String articleStatusName);


    @Query(value = "select  * from  article a  inner join information_article ia on ia.watdou='CONFIRM' and ia.chek_user_id=?1 and a.id=ia.article_id    and a.active=false", nativeQuery = true)
    List<Article> findAllByActiveFalseArticle(UUID id);


    List<Article> findAllByActiveTrueAndArticleStatusNameOrActiveTrueAndArticleStatusName(String articleStatusName, String articleStatusName2);
    @Query(value = "select a.* from article a  INNER JOIN information_article ia on ia.article_id=a.id and ia.article_status_name=?1\n" +
            "                        inner join editors_article ea on ia.redactor_id=ea.redactor_id  and  ea.redactor_id=?2 AND ea.article_id=a.id", nativeQuery = true)
    List<Article> findAllByRedactor(String statusName, UUID id);

    Integer countAllByPayTrueAndConfirmFalse();
    Integer countAllByPayFalseOrPayTrue();
    Integer countAllByArticleStatusName(ArticleStatusName articleStatusName);

    @Query(value = "select count(*) from article where article_status_name=?1 and public_private=?2", nativeQuery = true)
    Integer countAllByArticleStatusName(ArticleStatusName articleStatusName, boolean publicAndPrivate);

    @Query(value = "select count(*) from article where public_private=true and article_status_name=?1", nativeQuery = true)
    Integer countAllByPublicPrivateTrueAndArticleStatusName(ArticleStatusName articleStatusName);

    Integer countAllByPublicPrivateAndArticleStatusName(boolean publicPrivate, ArticleStatusName articleStatusName);

    Integer countAllByPayFalse();


    List<Article> findAllByArticleStatusNameAndUserId(ArticleStatusName articleStatusName, UUID user_id);

    @Query(value = "select * from article a inner join article_authors aa on a.id = aa.article_id and a.article_status_name=?2 inner join authors au on aa.authors_id = au.id and au.code=?1",nativeQuery = true)
    List<Article> findAllByAuthorsAndArticleStatusName(int code, String articleStatusName);

    List<Article> findAllByArticleStatusNameAndUserIdOrArticleStatusNameAndUserId(ArticleStatusName articleStatusName, UUID user_id, ArticleStatusName articleStatusName2, UUID user_id2);

    @Query(value = "select a.* from article a inner join article_authors aa on a.id=aa.article_id  and a.deleted=false inner join authors au on au.id=aa.authors_id and au.code=?1 ", nativeQuery = true)
    List<Article> findAllByAuthorsCode(int authors_code);

    @Query(value = "select  * from article a  inner join article_journals aj on aj.article_id=a.id and aj.journals_id=?1", nativeQuery = true)
    List<Article> journalArticles(UUID id);

    @Query(value = "select  * from article a  inner join journals j on j.id=?1 and a.article_status_name='PUBLISHED' and a.journals_active=true and a.deleted=false inner join article_journals aj on aj.journals_id=j.id and a.id=aj.article_id", nativeQuery = true)
    List<Article> journalArticlesForJournals(UUID id);

    List<Article> findAllByJournalsInAndPayTrue(Collection<List<Journals>> journals);

    @Query(value = "select a.* from article a inner join article_authors au  on article_status_name=?1   inner join users u on u.id=au.authors_id and u.code=?2 ", nativeQuery = true)
    List<Article> findAllByArticleStatusNameAndAuthorsCode(String articleStatusName, int code);



    Article findByDeletedTrueAndId(UUID id);
    Optional<Article> findByDeletedFalseAndId(UUID id);

Article findByActiveTrueAndId(UUID id);


    Integer countAllByPayTrue();
}

package com.example.article.repository;

import com.example.article.entity.Article;
import com.example.article.entity.enums.ArticleStatusName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    List<Article> findAllByCreatedBy(UUID id);
List<Article>findAllByIdIn(Collection<UUID> id);

    Page<Article> findByPayTrue(Pageable simplePageable);


//    Page<Article>findAllByChecked(String checked, Pageable pageable);

    Page<Article> findByActiveTrue(Pageable simplePageable);


    List<Article> findAllByPayTrueAndConfirmFalse();





    Article findByConfirmTrueAndId(UUID id);


    @Query(value = "select  * from  article a  inner join information_article ia on ia.watdou='CONFIRM' and ia.chek_user_id=?1 and a.id=ia.article_id  and a.article_status_name=?2 ",nativeQuery = true)
    List<Article> findAllByConfirmTrueAndAdmin(UUID id, String articleStatusName);


    @Query(value = "select a.* from article a  INNER JOIN information_article ia on ia.article_id=a.id and ia.article_status_name=?1\n" +
            "                        inner join editors_article ea on ia.redactor_id=ea.redactor_id  and  ea.redactor_id=?2 AND ea.article_id=a.id",nativeQuery = true)
    List<Article> findAllByRedactor(String statusName, UUID id);

    Integer countAllByPayTrueAndConfirmFalse();
    Integer countAllByArticleStatusName(ArticleStatusName articleStatusName);
//    @Query(value = "select count(*) from article where article_status_name=?1 and public_private=?2",nativeQuery = true)
//    Integer countAllByArticleStatusName(ArticleStatusName articleStatusName, boolean publicAndPrivate);

//    @Query(value = "select count(*) from article where public_private=true and article_status_name=?1",nativeQuery = true)
//    Integer countAllByPublicPrivateTrueAndArticleStatusName(ArticleStatusName articleStatusName);

    Integer countAllByPublicPrivateAndArticleStatusName(boolean publicPrivate, ArticleStatusName articleStatusName);
    Integer countAllByPayFalse();

    List<Article> findAllByArticleStatusNameAndUserId(ArticleStatusName articleStatusName, UUID user_id);
    List<Article> findAllByArticleStatusNameAndUserIdOrArticleStatusNameAndUserId(ArticleStatusName articleStatusName, UUID user_id, ArticleStatusName articleStatusName2, UUID user_id2);

    @Query(value = "select a.* from article a inner join article_authors aa on a.id=aa.article_id inner join authors au on au.id=aa.authors_id and au.code=?1 ",nativeQuery = true)
    List<Article> findAllByAuthorsCode(Integer authors_code);
}

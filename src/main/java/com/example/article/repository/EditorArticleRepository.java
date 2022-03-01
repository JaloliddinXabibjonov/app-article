package com.example.article.repository;

import com.example.article.entity.Article;
import com.example.article.entity.EditorsArticle;
import com.example.article.entity.Role;
import com.example.article.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EditorArticleRepository extends JpaRepository<EditorsArticle, UUID> {
    @Transactional
    @Modifying
    void deleteByArticleIdAndRedactorId(UUID article_id, UUID redactor_id);

    Optional<EditorsArticle> findByArticleIdAndRedactorId(UUID article_id, UUID redactor_id);

    boolean existsByArticleIdAndRedactorId(UUID article_id, UUID redactor_id);


    @Query(value = "select ea.*\n" +
            "from editors_article ea\n" +
            "         inner join information_article ia\n" +
            "                    ON IA.redactor_id = ?1\n" +
            "                        AND ea.redactor_id = ia.redactor_id\n" +
            "                        AND ea.article_id=IA.article_id and\n" +
            " ia.article_status_name != 'I_ACCEPTED' and ia.article_status_name != 'I_DID_NOT_ACCEPT'", nativeQuery = true)
    List<EditorsArticle> findAllByAndRedactorId(UUID redactor_id);

    @Query(value = "select count(*) from editors_article  where article_id=?1 and role_id=?2",nativeQuery = true)
    Integer countAllByArticleId(UUID article_id, Integer roleId);


    List<EditorsArticle> findAllByArticleIdAndRoleIdAndRedactorIdIn(UUID article_id, Integer roleId, List<UUID> redactor_id);


//    List<EditorsArticle> findAllByArticleIdAndRoleId(UUID article_id, Integer roleId);

    List<EditorsArticle>findAllByRedactorId(UUID redactor_id);

    List<User> findAllByArticleIdAndRoleId(UUID article_id, Integer roleId);

    //   Integer countAllByRedactorIdAnd


//    @Query(value = "select ed.redactor_id from editors_article  ed where ed.article_id=?1 ", nativeQuery = true)
//    List<UUID> findAllByArticleId(UUID articleId);


//@Query(value = "select * from  editors_article e where e.article_id=?1 and e.redactor_id=(select u.users_id from  users_roles u where  u.roles_id=(select r.id from role r where r.role_name=?2 limit 1))", nativeQuery = true)
//List<EditorsArticle> findAllByArticleIdAndRoleName(UUID articleId, String roleName);


//    @Query(value = "select * from editors_article e\n" +
//            "             inner join role r on r.role_name = ?2\n" +
//            "             inner join users_roles ur on ur.roles_id=r.id\n" +
//            "             inner join users_roles ur2 on ur.users_id=e.redactor_id\n" +
//            "            where e.article_id=?1",nativeQuery = true)
//    List<EditorsArticle> findAllByArticleIdAndRoleName(UUID articleId, String roleName);


}

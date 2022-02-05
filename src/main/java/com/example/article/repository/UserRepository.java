package com.example.article.repository;

import com.example.article.entity.Article;
import com.example.article.entity.Role;
import com.example.article.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhoneNumberAndDeleteFalse(String phoneNumber);

    List<User> findAllByEnabledTrueAndIdInAndDeleteFalse(Collection<UUID> id);

    User findAllByEnabledTrueAndIdAndDeleteFalse(UUID id);

    List<User> findAllByRolesIdAndDeleteFalse(Integer roles_id);

    List<User> findAllByActiveTrueAndDeleteFalseAndRolesIdAndCategoriesId(Integer roleId, Integer categories_id);

    Page<User> findAllByDeleteFalse(Pageable pageable);
    @Query(value = "select ur.roles_id from users_roles ur where users_id=?1", nativeQuery = true)
    Integer findByUserIdAndDeleteFalse(UUID id);

    List<User> findAllByEnabledTrueAndRolesIdAndCategoriesIdAndDeleteFalse(Integer roleId, Integer categories_id);

//    List<User> findAllByRolesIdAndCategoriesId(Integer roles_id, Integer categories_id);

    Page<User> findAllByRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndPhoneNumberContainingIgnoringCase(Integer roles_id, Collection<Integer> categories_id, boolean enabled, String firstName, Integer roles_id2, Collection<Integer> categories_id2, boolean enabled2, String lastName, Integer roles_id3, Collection<Integer> categories_id3, boolean enabled3, String fatherName, Integer roles_id4, Collection<Integer> categories_id4, boolean enabled4, String email, Integer roles_id5, Collection<Integer> categories_id5, boolean enabled5, String phoneNumber, Pageable pageable);

    Page<User> findAllByEnabledAndFirstNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndLastNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndFatherNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndEmailContainingIgnoringCaseAndDeleteFalseOrEnabledAndPhoneNumberContainingIgnoringCase(boolean enabled, String firstName,boolean enabled1,  String lastName, boolean enabled2, String fatherName, boolean enabled3, String email, boolean enabled4, String phoneNumber, Pageable pageable);

    Page<User> findAllByRolesIdAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(Integer roles_id, boolean enabled, String firstName, Integer roles_id2, boolean enabled2, String lastName, Integer roles_id3, boolean enabled3, String fatherName, Integer roles_id4, boolean enabled4, String email, Integer roles_id5, boolean enabled5, String phoneNumber, Pageable pageable);

    Page<User> findAllByCategoriesIdInAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(Collection<Integer> categories_id, boolean enabled, String firstName, Collection<Integer> categories_id2, boolean enabled2, String lastName, Collection<Integer> categories_id3, boolean enabled3, String fatherName, Collection<Integer> categories_id4, boolean enabled4, String email, Collection<Integer> categories_id5, boolean enabled5, String phoneNumber, Pageable pageable);

@Query(value = "select * from users u inner join  users_roles ur on u.id=ur.users_id and ur.roles_id=?2 and u.enabled=?1 and  u.delete=false",nativeQuery = true)
    Page<User> findAllByEnabledAndRolesIdAndDeleteFalse(boolean enabled, Integer roles_id,boolean delete, Pageable pageable);

    Page<User> findAllByEnabledAndDeleteFalseAndCategoriesIdIn(boolean enabled, Collection<Integer> categories_id, Pageable pageable);

    Page<User> findAllByEnabledAndDeleteFalseAndCategoriesIdInAndRolesId(boolean enabled, Collection<Integer> categories_id, Integer roles_id, Pageable pageable);

    Page<User> findAllById(UUID id, Pageable pageable);

    Page<User> findAllByEnabled(boolean enabled, Pageable pageable);

    boolean existsByPhoneNumberAndDeleteFalse(String phoneNumber);

    Integer countAllByCreatedAtBetween(Timestamp start, Timestamp end);

    List<User> findAllByEnabledFalseAndActiveFalseAndDeleteFalseAndRolesId(Integer roles_id);

    Integer countAllByEnabledFalseAndActiveFalseAndRolesIdAndDeleteFalse(Integer roles_id);

    @Query(value = "select (count(*)) from users_roles where roles_id=?1 ", nativeQuery = true)
    Integer countAllByRolesId(Integer roleId);


    boolean existsByEnabledAndIdAndDeleteFalse(boolean enabled, UUID id);

}

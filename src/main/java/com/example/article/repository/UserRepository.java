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

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAllByEnabledTrueAndIdIn(Collection<UUID> id);

    User findAllByEnabledTrueAndId(UUID id);
List<User>findAllByRolesId(Integer roles_id);
    List<User> findAllByActiveTrueAndRolesIdAndCategoriesId(Integer roleId, Integer categories_id);

    @Query(value = "select ur.roles_id from users_roles ur where users_id=?1", nativeQuery = true)
    Integer findByUserId(UUID id);

    List<User> findAllByEnabledTrueAndRolesIdAndCategoriesId(Integer roleId, Integer categories_id);

//    List<User> findAllByRolesIdAndCategoriesId(Integer roles_id, Integer categories_id);

    Page<User> findAllByRolesIdAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(Integer roles_id, Collection<Integer> categories_id, boolean enabled, String firstName, String lastName, String fatherName, String email, String phoneNumber, Pageable pageable);

    Page<User> findAllByEnabledAndFirstNameContainingIgnoringCaseOrEnabledAndLastNameContainingIgnoringCaseOrEnabledAndFatherNameContainingIgnoringCaseOrEnabledAndEmailContainingIgnoringCaseOrEnabledAndPhoneNumberContainingIgnoringCase(boolean enabled, String firstName,boolean enabled1,  String lastName, boolean enabled2, String fatherName, boolean enabled3, String email, boolean enabled4, String phoneNumber, Pageable pageable);

    Page<User> findAllByRolesIdAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(Integer roles_id, boolean enabled, String firstName, String lastName, String fatherName, String email, String phoneNumber, Pageable pageable);

    Page<User> findAllByCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(Collection<Integer> categories_id, boolean enabled, String firstName, String lastName, String fatherName, String email, String phoneNumber, Pageable pageable);


    Page<User> findAllByEnabledAndRolesId(boolean enabled, Integer roles_id, Pageable pageable);

    Page<User> findAllByEnabledAndCategoriesIdIn(boolean enabled, Collection<Integer> categories_id, Pageable pageable);

    Page<User> findAllByEnabledAndCategoriesIdInAndRolesId(boolean enabled, Collection<Integer> categories_id, Integer roles_id, Pageable pageable);

Page<User>findAllById(UUID id, Pageable pageable);
    boolean existsByPhoneNumber(String phoneNumber);

    Integer countAllByCreatedAtBetween(Timestamp start, Timestamp end);

    List<User> findAllByEnabledFalseAndActiveFalseAndRolesId(Integer roles_id);

    Integer countAllByEnabledFalseAndActiveFalseAndRolesId(Integer roles_id);

    @Query(value = "select (count(*)) from users_roles where roles_id=?1",nativeQuery = true)
    Integer countAllByRolesId(Integer roleId);



}

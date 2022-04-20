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

    boolean existsByCode(int code);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, UUID id);

    Optional<User> findByPhoneNumberAndDeleteFalse(String phoneNumber);

    List<User> findAllByEnabledTrueAndIdInAndDeleteFalse(Collection<UUID> id);

    User findAllByEnabledTrueAndIdAndDeleteFalse(UUID id);

    boolean existsByPasswordAndIdNot(String password, UUID id);

    User findByEnabledTrueAndId(UUID id);

    List<User> findAllByEnabled(boolean enabled);
    List<User> findAllByRolesIdAndDeleteFalse(Integer roles_id);

    List<User> findAllByActiveTrueAndDeleteFalseAndRolesIdAndCategoriesId(Integer roleId, Integer categories_id);

    Page<User> findAllByDeleteFalse(Pageable pageable);

    List<User> findAllByDeleteFalse();
    @Query(value = "select ur.roles_id from users_roles ur where users_id=?1", nativeQuery = true)
    Integer findByUserIdAndDeleteFalse(UUID id);

    //    @Query(value = "select * from users inner join users_roles on users.id = users_roles.users_id and users_roles.roles_id=?1 inner join users_categories uc on users.id=uc.users_id and uc.categories_id=?2 and users.delete=false",nativeQuery = true)
//    List<User> findAllByEnabledTrueAndRolesIdAndCategoriesIdAndDeleteFalse(Integer roleId, Integer categories_id);
    @Query(value = "select * from users u inner join users_languages ul on u.id = ul.users_id and ul.languages_id=?3 and u.enabled=true and active=true inner join users_roles ur on u.id = ur.users_id and ur.roles_id=?1 inner join users_categories uc on u.id = uc.users_id and uc.categories_id=?2 ",nativeQuery = true)
    List<User> findAllByEnabledTrueAndActiveTrueAndRolesIdAndCategoriesIdAndDeleteFalseAndLanguages(Integer roleId, Integer categories_id, Integer languageId);


    List<User> findAllByEnabledTrueAndRolesIdAndDeleteFalse(Integer roleId);
//    List<User> findAllByEnabledTrueAndActiveTrueAndRolesIdAndDeleteFalse(Integer roleId);
    @Query(value = "select * from users u inner join users_languages ul on u.id = ul.users_id and ul.languages_id=?2 and u.enabled=true and active=true inner join users_roles ur on u.id = ur.users_id and ur.roles_id=?1 ",nativeQuery = true)
    List<User> findAllByEnabledTrueAndActiveTrueAndRolesIdAndDeleteFalseAndLanguageId(Integer roleId, Integer languageId);


//    List<User> findAllByRolesIdAndCategoriesId(Integer roles_id, Integer categories_id);

//    Page<User> findAllByRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndPhoneNumberContainingIgnoringCase(Integer roles_id, Collection<Integer> categories_id, boolean enabled, String firstName, Integer roles_id2, Collection<Integer> categories_id2, boolean enabled2, String lastName, Integer roles_id3, Collection<Integer> categories_id3, boolean enabled3, String fatherName, Integer roles_id4, Collection<Integer> categories_id4, boolean enabled4, String email, Integer roles_id5, Collection<Integer> categories_id5, boolean enabled5, String phoneNumber, Pageable pageable);

    List<User> findAllByActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndLastNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFatherNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndEmailContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndPhoneNumberContainingIgnoringCase(Integer roles_id, Collection<Integer> categories_id, boolean enabled, String firstName, Integer roles_id2, Collection<Integer> categories_id2, boolean enabled2, String lastName, Integer roles_id3, Collection<Integer> categories_id3, boolean enabled3, String fatherName, Integer roles_id4, Collection<Integer> categories_id4, boolean enabled4, String email, Integer roles_id5, Collection<Integer> categories_id5, boolean enabled5, String phoneNumber);
//    Page<User> findAllByEnabledAndFirstNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndLastNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndFatherNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndEmailContainingIgnoringCaseAndDeleteFalseOrEnabledAndPhoneNumberContainingIgnoringCase(boolean enabled, String firstName, boolean enabled1, String lastName, boolean enabled2, String fatherName, boolean enabled3, String email, boolean enabled4, String phoneNumber, Pageable pageable);

    List<User> findAllByActiveTrueAndEnabledAndFirstNameContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndLastNameContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndFatherNameContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndEmailContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndPhoneNumberContainingIgnoringCase
            (boolean enabled, String firstName, boolean enabled2, String lastName, boolean enabled3, String fatherName, boolean enabled4, String email, boolean enabled5, String phoneNumber);
//    Page<User> findAllByRolesIdAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(Integer roles_id, boolean enabled, String firstName, Integer roles_id2, boolean enabled2, String lastName, Integer roles_id3, boolean enabled3, String fatherName, Integer roles_id4, boolean enabled4, String email, Integer roles_id5, boolean enabled5, String phoneNumber, Pageable pageable);
List<User> findAllByActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(Integer roles_id, boolean enabled, String firstName, Integer roles_id2, boolean enabled2, String lastName, Integer roles_id3, boolean enabled3, String fatherName, Integer roles_id4, boolean enabled4, String email, Integer roles_id5, boolean enabled5, String phoneNumber);
//    Page<User> findAllByCategoriesIdInAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(Collection<Integer> categories_id, boolean enabled, String firstName, Collection<Integer> categories_id2, boolean enabled2, String lastName, Collection<Integer> categories_id3, boolean enabled3, String fatherName, Collection<Integer> categories_id4, boolean enabled4, String email, Collection<Integer> categories_id5, boolean enabled5, String phoneNumber, Pageable pageable);

    List<User> findAllByActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(Collection<Integer> categories_id, boolean enabled, String firstName, Collection<Integer> categories_id2, boolean enabled2, String lastName, Collection<Integer> categories_id3, boolean enabled3, String fatherName, Collection<Integer> categories_id4, boolean enabled4, String email, Collection<Integer> categories_id5, boolean enabled5, String phoneNumber);
//    @Query(value = "select * from users u inner join  users_roles ur on u.id=ur.users_id and ur.roles_id=?2 and u.enabled=?1 and  u.delete=false", nativeQuery = true)
//    Page<User> findAllByEnabledAndRolesIdAndDeleteFalse(boolean enabled, Integer roles_id, boolean delete, Pageable pageable);

    @Query(value = "select * from users u inner join  users_roles ur on u.id=ur.users_id and ur.roles_id=?2 and u.enabled=?1 and  u.delete=false", nativeQuery = true)
    List<User> findAllActiveTrueAndByEnabledAndRolesIdAndDeleteFalse(boolean enabled, Integer roles_id, boolean delete);

//    Page<User> findAllByEnabledAndDeleteFalseAndCategoriesIdIn(boolean enabled, Collection<Integer> categories_id, Pageable pageable);
List<User> findAllByActiveTrueAndEnabledAndDeleteFalseAndCategoriesIdIn(boolean enabled, Collection<Integer> categories_id);
//    Page<User> findAllByEnabledAndDeleteFalseAndCategoriesIdInAndRolesId(boolean enabled, Collection<Integer> categories_id, Integer roles_id, Pageable pageable);
List<User> findAllByActiveTrueAndEnabledAndDeleteFalseAndCategoriesIdInAndRolesId(boolean enabled, Collection<Integer> categories_id, Integer roles_id);

   List<User>findAllByEnabledAndActiveFalse(boolean enabled);

    Page<User> findAllById(UUID id, Pageable pageable);

    Page<User> findAllByEnabled(boolean enabled, Pageable pageable);

    boolean existsByPhoneNumberAndDeleteFalse(String phoneNumber);

    Integer countAllByCreatedAtBetween(Timestamp start, Timestamp end);

    List<User> findAllByEnabledFalseAndActiveFalseAndDeleteFalseAndRolesId(Integer roles_id);

    Integer countAllByEnabledFalseAndActiveFalseAndRolesIdAndDeleteFalse(Integer roles_id);

    @Query(value = "select (count(*)) from users_roles where roles_id=?1 ", nativeQuery = true)
    Integer countAllByRolesId(Integer roleId);


    boolean existsByEnabledAndIdAndDeleteFalse(boolean enabled, UUID id);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByCode(Integer code);

    Optional<User> findByIdAndDeleteFalse(UUID id);

    @Query(value = "select * from users u inner join users_roles ur on u.id = ur.users_id and ur.roles_id=3 and u.active=false",nativeQuery = true)
    List<User> findAllByActiveFalse();
}

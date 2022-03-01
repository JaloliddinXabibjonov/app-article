package com.example.article.repository;

import com.example.article.entity.NotificationFromUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface NotificationFromUserRepository extends JpaRepository<NotificationFromUser, Integer> {

    Integer countAllByAdministratorIdAndReadFalse(UUID administratorId);

    Integer countAllByUserIdAndReadFalse(UUID userId);

    List<NotificationFromUser> findAllByAdministratorIdAndReadFalse(UUID administratorId);

    List<NotificationFromUser> findAllByUserIdAndReadFalse(UUID userId);

    @Transactional
    @Modifying
    void deleteById(Integer id);


}

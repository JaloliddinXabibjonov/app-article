package com.example.article.repository;

import com.example.article.entity.VerifyPassword;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerifyPasswordRepository extends JpaRepository<VerifyPassword, UUID> {

    Optional<VerifyPassword> findByVerifyCodeAndPhoneNumber(Integer verifyCode, String phoneNumber);
}

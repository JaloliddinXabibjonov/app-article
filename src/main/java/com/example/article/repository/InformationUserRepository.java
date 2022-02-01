package com.example.article.repository;

import com.example.article.entity.InformationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InformationUserRepository extends JpaRepository<InformationUser, UUID> {


}

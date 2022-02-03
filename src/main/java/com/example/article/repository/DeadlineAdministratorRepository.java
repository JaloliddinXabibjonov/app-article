package com.example.article.repository;

import com.example.article.entity.DeadlineAdministrator;
import com.example.article.entity.DeadlineDefaultValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadlineAdministratorRepository extends JpaRepository<DeadlineAdministrator, Integer> {


}

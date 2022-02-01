package com.example.article.repository;

import com.example.article.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface   CategoryRepository extends JpaRepository<Category, Integer> {

    Category  getById(Integer id);
    List<Category> findAllByNameContainingIgnoringCase(String name);

boolean existsByName(String name);

    List<Category> findAllByIdIn(Set<Integer> categoryIdList);

    List<Category>findAllById(Integer id);


}

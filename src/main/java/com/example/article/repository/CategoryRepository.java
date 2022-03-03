package com.example.article.repository;

import com.example.article.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category getById(Integer id);

    Category getByDeletedTrueAndId(Integer id);

    List<Category> findAllByDeletedTrueAndNameContainingIgnoringCase(String name);

    boolean existsByNameAndDeletedTrue(String name);

    List<Category> findAllByDeletedTrueAndIdIn(Set<Integer> categoryIdList);

    Page<Category> findAllByDeletedTrue(Pageable pageable);
List<Category>findAllByDeletedTrue();



    @Query(value = "select * from category where parent_id IS NULL", nativeQuery = true)
    List<Category> parentCategory();
}

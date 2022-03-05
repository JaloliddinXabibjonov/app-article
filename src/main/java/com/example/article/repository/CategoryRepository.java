package com.example.article.repository;

import com.example.article.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

//    Category getById(Integer id);

    Category getByIdAndDeletedTrue(Integer id);
    Category getByDeletedTrueAndActiveTrueAndId(Integer id);

    List<Category> findAllByDeletedTrueAndNameContainingIgnoringCase(String name);

    boolean existsByNameAndDeletedTrueAndActiveTrue(String name);

    List<Category> findAllByDeletedTrueAndActiveTrueAndIdIn(Set<Integer> categoryIdList);

    Page<Category> findAllByDeletedTrueAndActiveTrue(Pageable pageable);

    List<Category> findAllByDeletedTrueAndActiveTrue();


    @Query(value = "select * from category where  deleted=true and parent_id IS NULL and active=true", nativeQuery = true)
    List<Category> parentCategory();

    List<Category> findAllByDeletedTrueAndActiveTrueAndParentId(Integer parent_id);

    Category getByDeletedTrueAndId(Integer id);

    List<Category> findAllByDeletedTrue();
}

package com.example.article.repository;

import com.example.article.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category getById(Integer id);

    List<Category> findAllByNameContainingIgnoringCase(String name);

    boolean existsByName(String name);

    List<Category> findAllByIdIn(Set<Integer> categoryIdList);

    List<Category> findAllById(Integer id);

    List<Category> findAllByParentIdIn(List<Integer> parent_id);

    List<Category> findAllByParentAndIdIn(Category parent, Collection<Integer> id);


    List<Category> findAllByParent(Category parent);


    @Query(value = "select * from category where parent_id IS NULL", nativeQuery = true)
    List<Category> parentCategory();
}

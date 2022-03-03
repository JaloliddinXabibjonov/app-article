package com.example.article.servise;

import com.example.article.entity.Category;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.CategoryDto;
import com.example.article.repository.CategoryRepository;
import com.example.article.utils.CommonUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public ApiResponse saveOrEdit(CategoryDto dto) {
        boolean exists = categoryRepository.existsByNameAndDeletedTrue(dto.getName());
        if (exists)
            return new ApiResponse(dto.getName() + " nomli bo`lim avval qo`shilgan", false);
        try {
            Category category = new Category();
            if (dto.getId() != null) {
                category = categoryRepository.getByDeletedTrueAndId(dto.getId());
            }
            if (dto.getParentDto() != null && dto.getParentDto().getId() > 0) {
                category.setParent(categoryRepository.getByDeletedTrueAndId(dto.getParentDto().getId()));
            }
            category.setName(dto.getName());
            category.setActive(dto.getId() == null || category.isActive());
            category.setDeleted(true);
            categoryRepository.save(category);
            return new ApiResponse(dto.getId() != null ? "Edited" : "Saved", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi, qaytadan urinib ko`ring", false);
        }
    }

    public ApiResponse allByPageable(Integer page, Integer size, String search) throws IllegalAccessException {
        List<Category> categories = new ArrayList<>();
        long totalElements = 0;
        if (!search.equals("all")) {
            categories = categoryRepository.findAllByDeletedTrueAndNameContainingIgnoringCase(search);
        } else {
            if (size > 0) {
                Page<Category> categoryPage = categoryRepository.findAllByDeletedTrue(CommonUtills.getPageableByIdDesc(page, size));
                categories = categoryPage.getContent();
                totalElements = categoryPage.getTotalElements();
            } else {
                categories = categoryRepository.findAllByDeletedTrue();
            }
        }
        return new ApiResponse(true, "CategoryPage", categories.stream().map(this::getCategoryDto).collect(Collectors.toList()), totalElements);
    }

    public CategoryDto getCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setActive(category.isActive());
        if (category.getParent() != null) {
            dto.setParentDto(getCategoryDto(category.getParent()));
        }
        return dto;
    }

    public ApiResponse changeActive(Integer id) {
        Category byId = categoryRepository.getByDeletedTrueAndId(id);
        byId.setActive(!byId.isActive());
        categoryRepository.save(byId);
        return new ApiResponse(byId.isActive() ? "Activated" : "Blocked", true);
    }

    public ApiResponse remove(Integer id) {

        Category category = categoryRepository.getByDeletedTrueAndId(id);
        category.setDeleted(false);
        categoryRepository.save(category);

        return new ApiResponse("Ok", true);
    }


    public List<Category> all() {
        return categoryRepository.parentCategory();
    }
}
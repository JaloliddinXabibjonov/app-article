package com.example.article.servise;

import com.example.article.entity.Category;
import com.example.article.entity.Journals;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.CategoryDto;
import com.example.article.repository.CategoryRepository;
import com.example.article.repository.JournalsRepository;
import com.example.article.utils.CommonUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    JournalsRepository journalsRepository;
    public ApiResponse saveOrEdit(CategoryDto dto) {
        boolean exists = categoryRepository.existsByNameAndDeletedTrueAndActiveTrue(dto.getName());
//        if (exists)
//            return new ApiResponse(dto.getName() + " nomli bo`lim avval qo`shilgan", false);
        try {
            Category category = new Category();
            if (dto.getId() != null) {
                category = categoryRepository.getByDeletedTrueAndActiveTrueAndId(dto.getId());
            }
            if (dto.getParentId() != null && dto.getParentId() > 0) {

                category.setParent(categoryRepository.getByDeletedTrueAndActiveTrueAndId(dto.getParentId()));
            }
            System.out.println(dto.isActive());
            category.setName(dto.getName());
            category.setActive(dto.isActive());
            category.setDeleted(true);
            System.out.println(dto.getParentId());
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
                Page<Category> categoryPage = categoryRepository.findAllByDeletedTrueAndActiveTrue(CommonUtills.getPageableByIdDesc(page, size));
                categories = categoryPage.getContent();
                totalElements = categoryPage.getTotalElements();
            } else {
                categories = categoryRepository.findAllByDeletedTrueAndActiveTrue();
            }
        }
//        return new ApiResponse(true, "CategoryPage", categories.stream().map(this::getCategoryDto).collect(Collectors.toList()), totalElements);
        return new ApiResponse( "CategoryPage",true);
    }

//    public CategoryDto getCategoryDto(Category category) {
//        CategoryDto dto = new CategoryDto();
//        dto.setId(category.getId());
//        dto.setName(category.getName());
//        dto.setActive(category.isActive());
//        if (category.getParent() != null) {
//            dto.setParentDto(getCategoryDto(category.getParent()));
//        }
//        return dto;
//    }

    public ApiResponse changeActive(Integer id) {
        Category byId = categoryRepository.getByDeletedTrueAndId(id);
        byId.setActive(!byId.isActive());
        categoryRepository.save(byId);
        return new ApiResponse(byId.isActive() ? "Activated" : "Blocked", true);
    }

    public ApiResponse remove(Integer id) {

        Category category = categoryRepository.getByIdAndDeletedTrue(id);
        category.setDeleted(false);
        categoryRepository.save(category);

        return new ApiResponse("Ok", true);
    }


    public List<Category> all() {
        return categoryRepository.findAllByDeletedTrue();
    }


    public List<Category> allParentCategory() {
        return categoryRepository.parentCategory();
    }


    public List<Category> allChildrenCategory(UUID journalId) {
        Optional<Journals> optionalJournals = journalsRepository.findByDeletedTrueAndId(journalId);
        if (optionalJournals.isPresent()){
            Integer id = optionalJournals.get().getCategory().getId();
            return categoryRepository.findAllByDeletedTrueAndActiveTrueAndParentId(id);
        }
        return new ArrayList<>();
    }
}
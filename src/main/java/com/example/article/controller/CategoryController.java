package com.example.article.controller;

import com.example.article.entity.Category;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.CategoryDto;
import com.example.article.servise.CategoryService;
import com.example.article.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/saveOrEdit")
    public HttpEntity<ApiResponse> saveOrEdit(@RequestBody CategoryDto categoryDto) {
        ApiResponse apiResponse = categoryService.saveOrEdit(categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/allByPageable")
    public HttpEntity<ApiResponse> allByPageable(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                 @RequestParam(value = "search", defaultValue = "all") String search
    ) throws IllegalAccessException {
        return ResponseEntity.ok(categoryService.allByPageable(page, size, search));
    }

    /** KATEGORIYANI AKTIVLASHTIRISH VA BLOKLASH */
    @GetMapping("/changeActive/{id}")
    public HttpEntity<ApiResponse> changeActive(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.changeActive(id));
    }

    /** KATEGORIYANI O'CHIRISH */
    @GetMapping("/delete/{id}")
    public HttpEntity<ApiResponse> remove(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.remove(id));
    }

    /** BARCHA KATEGORIYALARNI OLIB KELISH */
    @GetMapping("/all")
    public List<Category> forJournals() {
        return categoryService.all();
    }


    /** BARCHA PARENT KATEGORIYALARNI OLIB KELISH UCHUN */
    @GetMapping("/allParentCategory")
    public List<Category> allParentCategory() {
        return categoryService.allParentCategory();
    }

    /** BARCHA CHOP ETILGAN JURNALLARNI  KATEGORIYALARNI OLIB KELISH UCHUN */
    @GetMapping("/parentCategories")
    public HttpEntity<ApiResponse> parentCategories() {
        ApiResponse apiResponse = categoryService.parentCategories();
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**  */
    @GetMapping("/allChildrenCategory/{journalId}")
    public List<Category> allChildrenCategory(@PathVariable UUID journalId){
        return categoryService.allChildrenCategoryByJournalId(journalId);
    }

    @GetMapping("/categoriesForAddReviewers")
    public List<Category> categoriesForAddReviewers(){
        return categoryService.categoriesForAddReviewers();
    }

}

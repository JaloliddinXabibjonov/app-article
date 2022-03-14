package com.example.article.controller;


import com.example.article.entity.Article;
import com.example.article.entity.Category;
import com.example.article.entity.Journals;
import com.example.article.payload.*;
import com.example.article.servise.JournalsService;
import com.google.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/journals")
public class JournalsController {
    @Autowired
    JournalsService journalsService;


    @PostMapping("/addJournals")
    public HttpEntity<ApiResponse> addJournals(@ModelAttribute JournalsPayload journalsPayload, @RequestPart(required = false) MultipartFile cover, @RequestPart(required = false) MultipartFile file) throws IOException {
        ApiResponse apiResponse = journalsService.addNewJournal(journalsPayload, cover, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PostMapping("/edit/{id}")
    public HttpEntity<ApiResponse> edit(@PathVariable UUID id, @ModelAttribute JournalsPayload journalsPayload, @RequestPart(required = false) MultipartFile cover, @RequestPart(required = false) MultipartFile file) {
        ApiResponse apiResponse = journalsService.edit(id, journalsPayload, cover, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping("/getActiveJournals")
    public List<ActiveJournalsDto> getActiveJournals() {
        return journalsService.getActiveJournals();
    }

    @GetMapping("/getCategoryJournals/{id}")
    public List<ActiveJournalsDto> getCategoryJournals(@PathVariable Integer id) {
        return journalsService.getCategoryJournals(id);
    }

    @GetMapping("/getParentJournals")
    public List<Journals> getParentJournals() {
        return journalsService.getParentJournals();
    }


    @GetMapping("/getById/{id}")
    public HttpEntity<ApiResponse> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = journalsService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getArticlesFromMagazine/{id}")
    public List<Article> getArticlesFromMagazine(@PathVariable UUID id) {
        return journalsService.getArticlesFromMagazine(id);
    }

    @GetMapping("/getAllJournals/{id}")
    public List<Journals> getAllJournals(@PathVariable UUID id) {
        return journalsService.getAllJournals(id);
    }


    @GetMapping("/getYear/{id}")
    public List<Integer> getYear(@PathVariable UUID id) {
        return journalsService.getYear(id);
    }


    @GetMapping("/getYearJournals/{year}/{id}")
    public List<ActiveJournalsDto> getYearJournals(@PathVariable Integer year,@PathVariable UUID id) {
        return journalsService.getYearJournals(id,year);
    }

    @GetMapping("/getJournalInfo/{id}")
    public JournalInfo getJournalInfo(@PathVariable UUID id){
        return journalsService.getJournalInfo(id);
    }

    @GetMapping("/delete/{id}")
    public HttpEntity<ApiResponse> delete(@PathVariable UUID id){
        ApiResponse apiResponse = journalsService.deleteJournals(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping("/attachArticleToJournal/{id}/{action}")
    public HttpEntity<ApiResponse> attachArticleToJournal(@PathVariable UUID id, @PathVariable boolean action){
        ApiResponse apiResponse=journalsService.attachArticleToJournal(id,action);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

}

package com.example.article.controller;


import com.example.article.entity.Category;
import com.example.article.entity.Journals;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.JournalsPayload;
import com.example.article.servise.JournalsService;
import com.google.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    public HttpEntity<ApiResponse> edit(@PathVariable UUID id, @ModelAttribute JournalsPayload journalsPayload, @RequestPart(required = false) MultipartFile cover, @RequestPart MultipartFile file) {
        ApiResponse apiResponse = journalsService.edit(id, journalsPayload, cover, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping("/getActiveJournals")
    public List<Journals> getActiveJournals() {
        return journalsService.getActiveJournals();
    }

    @GetMapping("/getCategoryJournals/{id}")
    public List<Journals> getCategoryJournals(@PathVariable Integer id) {
        return journalsService.getCategoryJournals(id);
    }


    @GetMapping("/getParentJournals")
    public List<Journals> getParentJournals() {
        return journalsService.getParentJournals();
    }

    @GetMapping("/getById/{id}")
    public HttpEntity<ApiResponse> getById(@PathVariable UUID id){
        ApiResponse apiResponse = journalsService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}

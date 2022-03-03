package com.example.article.controller;


import com.example.article.entity.Category;
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

@RestController
@RequestMapping("/api/journals")
public class JournalsController {
    @Autowired
    JournalsService journalsService;

    @GetMapping("/getCategoryJournals")
    public List<Category> getCategoryJournals() {
        return journalsService.getCategoryJournals();
    }

    @PostMapping("/addJournals")
    public HttpEntity<ApiResponse> addJournals(@RequestBody JournalsPayload journalsPayload,@RequestPart MultipartFile photo, @RequestPart(required = false) MultipartFile file ) throws IOException {
        ApiResponse apiResponse = journalsService.addNewJournal(journalsPayload, photo, file);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

}
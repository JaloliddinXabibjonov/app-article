package com.example.article.controller;

import com.example.article.servise.FileReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/fileRead")
public class FileReadController {
    @Autowired
    FileReadService fileReadService;

    @GetMapping("getFile/{id}")
    public HttpEntity<?> fileRead(@PathVariable UUID id) throws IOException {

        return fileReadService.byteFileQuality(id);
    }
}

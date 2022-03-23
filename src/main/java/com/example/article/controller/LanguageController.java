package com.example.article.controller;

import com.example.article.entity.Languages;
import com.example.article.payload.ApiResponse;
import com.example.article.servise.LanguageService;
import com.google.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import java.util.List;

@RestController
@RequestMapping("/api/language")
public class LanguageController {

    @Autowired
    LanguageService languageService;

    /** BARCHA AKTIV TILLARNI OLIB KELISH */
    @GetMapping("/all")
    public List<Languages> allActiveLanguages(){
        return languageService.all();
    }

    /**  */
    @PutMapping("/edit/{id}/{name}")
    public HttpEntity<ApiResponse> edit(@PathVariable Integer id, @PathVariable String name){
        ApiResponse apiResponse =languageService.edit(id,name);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<ApiResponse> delete(@PathVariable Integer id){
        ApiResponse apiResponse=languageService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping("/add/{name}")
    public HttpEntity<?> add(@PathVariable String name){
        ApiResponse apiResponse=languageService.add(name);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @GetMapping("/allActives")
    public List<Languages> allActives(){
        return languageService.allActives();
    }

    @PostMapping("/changeActive/{id}/{active}")
    public HttpEntity<?> add(@PathVariable Integer id, @PathVariable boolean active){
        ApiResponse apiResponse=languageService.changeActive(id,active);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }
}

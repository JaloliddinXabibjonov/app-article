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


    /**
     * YANGI JURNAL QO`SHISH
     */
    @PostMapping("/addJournals")
    public HttpEntity<ApiResponse> addJournals(@ModelAttribute JournalsPayload journalsPayload, @RequestPart(required = false) MultipartFile cover, @RequestPart(required = false) MultipartFile file) throws IOException {
        ApiResponse apiResponse = journalsService.addNewJournal(journalsPayload, cover, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /**
     * JURNALNI TAHRIRLASH
     */
    @PostMapping("/edit/{id}")
    public HttpEntity<ApiResponse> edit(@PathVariable UUID id, @ModelAttribute JournalsPayload journalsPayload, @RequestPart(required = false) MultipartFile cover, @RequestPart(required = false) MultipartFile file) {
        ApiResponse apiResponse = journalsService.edit(id, journalsPayload, cover, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    /**
     * MAQOLA QABUL QILADIGAN JURNALLARNI OLIB KELISH UCHUN
     */
    @GetMapping("/getActiveJournals")
    public List<ActiveJournalsDto> getActiveJournals() {
        return journalsService.getActiveJournals();
    }

    /**
     * SHU KATEGORIYALI JURNALLARNI OLIB KELISH
     */
    @GetMapping("/getCategoryJournals/{id}")
    public List<ActiveJournalsDto> getCategoryJournals(@PathVariable Integer id) {
        return journalsService.getCategoryJournals(id);
    }

    /**
     * SHU KATEGORIYALI CHOP ETILGAN JURNALLARNI OLIB KELISH
     */
    @GetMapping("/getPublishedCategoryJournals/{id}")
    public List<ActiveJournalsDto> getPublishedCategoryJournals(@PathVariable Integer id) {
        return journalsService.getPublishedCategoryJournals(id);
    }

    /**
     * KATEGORIYASIGA QARAB JURNALLARNI OLIB KELISH
     */
    @GetMapping("/getCategoryJournalsForUsers/{id}")
    public List<ActiveJournalsDto> getCategoryJournalsForUsers(@PathVariable Integer id) {
        return journalsService.getCategoryJournalsForUsers(id);
    }

    /**
     * PARENT ID SI NULL BO`LGAN JURNALLARNI OLIB KELISH
     */
    @GetMapping("/getParentJournals")
    public List<Journals> getParentJournals() {
        return journalsService.getParentJournals();
    }

    /**
     * PARENT ID SI NULL VA CHOP ETILGAN JURNALLARNI OLIB KELISH
     */
    @GetMapping("/getPublishedParentJournals")
    public List<Journals> getPublishedParentJournals() {
        return journalsService.getPublishedParentJournals();
    }

    /**
     * MAQOLA QABUL QILISH MUDDATI O`TGAN JURNALLARNI OLIB KELISH
     */
    @GetMapping("/deadlineOver")
    public List<ActiveJournalsDto> deadlineOver() {
        return journalsService.deadlineOver();
    }

    /**
     * ID ORQALI JURNALNI OLIB KELISH
     */
    @GetMapping("/getById/{id}")
    public HttpEntity<ApiResponse> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = journalsService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * JURNALNI ID SI ORQALI UNDAGI MAQOLALARNI  OLIB KELISH
     */
    @GetMapping("/getArticlesFromMagazine/{id}")
    public List<Article> getArticlesFromMagazine(@PathVariable UUID id) {
        return journalsService.getArticlesFromMagazine(id);
    }

    /**
     * PARENT ID SI SHUNAQA YOKI ID SI SHUNAQA BO`LGAN JURNALLARNI OLIB KELISH
     */
    @GetMapping("/getAllJournals/{id}")
    public List<Journals> getAllJournals(@PathVariable UUID id) {
        return journalsService.getAllJournals(id);
    }


    /**
     * SHU ID LI JURNALNI QAYSI YILLARDA CHIQQANLIGINI OLIB KELISH UCHUN
     */
    @GetMapping("/getYear/{id}")
    public List<Integer> getYear(@PathVariable UUID id) {
        return journalsService.getYear(id);
    }

    /**
     * SHU ID LI JURNALNI CHOP ETILGAN YILLARINI OLIB KELISH UCHUN
     */
    @GetMapping("/getPublishedYears/{id}")
    public List<Integer> getPublishedYears(@PathVariable UUID id) {
        return journalsService.getPublishedYears(id);
    }

    /**
     * BERILGAN ID LI JURNALNI BERILGAN YILDAGI SONLARINI OLIB KELISH UCHUN
     */
    @GetMapping("/getYearJournals/{year}/{id}")
    public List<ActiveJournalsDto> getYearJournals(@PathVariable Integer year, @PathVariable UUID id) {
        return journalsService.getYearJournals(id, year);
    }

    /**
     * JURNAL HAQIDA UMUMIY MA'LUMOT(MAQOLALARI, NOMI, ...)
     */
    @GetMapping("/getJournalInfo/{id}")
    public JournalInfo getJournalInfoForUsers(@PathVariable UUID id) {
        return journalsService.getJournalInfoForUsers(id);
    }

    /**
     * JURNALNI O'CHIRISH UCHUN
     */
    @GetMapping("/delete/{id}")
    public HttpEntity<ApiResponse> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = journalsService.deleteJournals(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * MAQOLANI JURNAlGA BIRIKTIRISH UCHUN
     */
    @PostMapping("/attachArticleToJournal/{id}/{action}")
    public HttpEntity<ApiResponse> attachArticleToJournal(@PathVariable UUID id, @PathVariable boolean action) {
        ApiResponse apiResponse = journalsService.attachArticleToJournal(id, action);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BERILGAN ID LI JURNALNI BERILGAN YILDAGI CHOP ETILGAN SONLARINI OLIB KELISH UCHUN
     */
    @GetMapping("/getPublishedJournalsByYear/{id}/{year}")
    public List<ActiveJournalsDto> getPublishedJournalByYear(@PathVariable UUID id, @PathVariable Integer year) {
        return journalsService.getPublishedJournalsByYear(id, year);
    }


}

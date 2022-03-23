package com.example.article.controller;

import ch.qos.logback.classic.sift.AppenderFactoryUsingJoran;
import com.example.article.entity.Article;
import com.example.article.entity.User;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.payload.*;
import com.example.article.secret.CurrentUser;
import com.example.article.servise.ArticleService;
import com.example.article.servise.StatusArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/article")

public class ArticleController {
    @Autowired
    ArticleService articleService;

    @Autowired
    StatusArticleService statusArticleService;

    /** YANGI MAQOLA QO`SHISH */
    @PostMapping(value = "/addArticle")
    public HttpEntity<ApiResponse> save(@ModelAttribute AddArticleDto dto, @CurrentUser User user, @RequestPart MultipartFile file) throws IOException {
        ApiResponse apiResponse = articleService.addArticle(dto, user, file);
        System.out.println("------" + dto.getAuthorsList().toString());
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /** MAQOLANI TAHRIRLASH */
    @PostMapping("/edit")
    public HttpEntity<ApiResponse> editArticle(@ModelAttribute ArticleDto articleDto,@RequestPart(required = false) MultipartFile file  ) throws IOException {
        ApiResponse apiResponse = articleService.editArticle(articleDto, file);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    /** O'ZI YUKLAGAN MAQOLALARNI OLIB KELISH */
    @GetMapping("/getMyArticle")
    public HttpEntity<?> getMyArticle(@CurrentUser User user) {
        return ResponseEntity.ok(articleService.getMyArticle(user));
    }

    /**adminstratorlar uchun articlelarni statusiga qarab get qilish */
    @PostMapping("/newMyArticle")
    public ApiResponse newMyArticle(@CurrentUser User user, @RequestBody ArticleStatusInAdmins articleStatusInAdmins) {
        return articleService.newMyArticle(user, articleStatusInAdmins);
    }

    /**MAQOLANI ID SI ORQALI ORQALI O`CHIRISH*/
    @DeleteMapping("/deleteArticle/{id}")
    public HttpEntity<?> deleteMyArticle(@PathVariable UUID id) {
        ApiResponse apiResponse = articleService.deleteArticle(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**MAQOLANI ADMIN TOMONIDAN REDACTOR VA REVIEWER LARGA BIRIKTIRISH*/
    @PostMapping("/addAndRemoveRedactor")
    public HttpEntity<ApiResponse> addAndRemoveRedactor(@CurrentUser User user, @RequestBody AddRedactorDto addRedactorDto) {
        ApiResponse apiResponse = articleService.addAndRemoveRedactor(user, addRedactorDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**REVIEWER VA REDACTORLAR TOMONIDAN MAQOLALARNI QABUL QILISH */
    @PostMapping("/reviewerAcceptTheArticle")
    public HttpEntity<ApiResponse> reviewerAcceptTheArticle(@CurrentUser User user, @RequestBody ReviewerAndRedactorResponseDto redactorResponseDto) {
        ApiResponse apiResponse = articleService.reviewerAcceptTheArticle(user, redactorResponseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


//        @PostMapping("/editorAndReviewerResponsesToTheArticle")
//    public HttpEntity<ApiResponse> getArticleInformation(@CurrentUser User user,@RequestBody ReviewerAndRedactorResponseDto redactorResponseDto) {
//        ApiResponse apiResponse = articleService.editorAndReviewerResponsesToTheArticle(user,redactorResponseDto);
//
//        return ResponseEntity.status(apiResponse.isSuccess() ? apiResponse.getMessage()
//                                                                          .equals("Saved") ? 201 : 202 : 409)
//                             .body(apiResponse);
//
//    }


//    @PostMapping("/sendMassageDeadlineRedactorReviewer")
//    public HttpEntity<ApiResponse> sendMassageDeadlineRedactorReviewer(@CurrentUser User user, @RequestBody AddRedactorDto addRedactorDto) {
//        ApiResponse apiResponse = articleService.sendMassageDeadlineRedactorReviewer(user, addRedactorDto);
//
//        return ResponseEntity.status(apiResponse.isSuccess() ? apiResponse.getMessage()
//                        .equals("Saved") ? 201 : 202 : 409)
//                .body(apiResponse);
//
//    }


    /**ADMIN PANELIDAGI TAQSIMLANGAN MAQOLALAR BO`LIMIGA MAQOLALARNI KIMLARGA TAQSIMLANGANINI KO`RSATISH UCHUN*/
    @GetMapping("/distributed")
    public List<Article> getDistributedArticles(@CurrentUser User user) {
        return articleService.getDistributeds(user);
    }

    /**ADMIN TOMONIDAN MAQOLALARNI BIRIKTIRISH UCHUN REDACTOR VA REVIEWERLARNI OLIB KELISH UCHUN */
    @PostMapping("/getReviewerAndRedactorRandom")
    public ApiResponse getReviewerAndRedactorRandom(@RequestBody GetUsersRoleId getUsersRoleId) {
        return articleService.getReviewerAndRedactorRandom(getUsersRoleId);
    }

    /** ADMINGA YANGI QO`SHILGAN MAQOLALARNI OLIB KELISH UCHUN */
    @GetMapping("/getNewAllArticle")
    public ApiResponse getNewOllArticle() {
        return articleService.getNewOllArticle();
    }

    /**ADMINGA NECHTA YANGI MAQOLA QO`SHILGANLIGINI QAYTARADI */
    @GetMapping("/newArticlesNumber")
    public HttpEntity<?> newArticles() {
        Integer integer = articleService.getNumberNewArticles();
        return ResponseEntity.ok(integer);
    }

    /** REDACTOR VA REVIEWERLAR TEKSHIRAYOTGAN MAQOLALARNI OLIB KELISH UCHUN*/
    @PostMapping("/myDuties")
    public HttpEntity<?> myDuties(@CurrentUser User user) {
        ApiResponse apiResponse = articleService.myDuties(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**REDACTOR VA REVIEWERLARGA BIRIKTIRILGAN YANGI MAQOLALARNI OLIB KELISH UCHUN */
    @PostMapping("/myNewArticles")
    public HttpEntity<?> myNewArticles(@CurrentUser User user) {
        return ResponseEntity.ok(articleService.myNewArticles(user));
    }

//    /** REDACTOR VA REVIEWERLAR TEKSHIRAYOTGAN MAQOLALARI  */
//    @GetMapping("/getMyTasks")
//    public List<MyTasksDto> getMyTasks(@CurrentUser User user) {
//        return articleService.getMyTasks(user);
//    }

    /** ADMIN TOMONIDAN MAQOLAGA STATUS BERISH */
    @PostMapping("/givenStatus")
    public HttpEntity<?> statusesGivenToTheArticleByTheEditors(@RequestParam UUID userId, @RequestParam(required = false) String description, @RequestParam UUID articleId, @RequestParam String status, @RequestPart(required = false) MultipartFile file) {
        ApiResponse apiResponse = articleService.statusesGivenToTheArticleByTheEditors(userId, description, articleId, status, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**  */
    @PostMapping("/articleInformation")
    public ApiResponse articleInformation(@RequestBody ReviewerAndRedactorResponseDto responseDto) {
        return new ApiResponse("ok", true, articleService.informationArticle(responseDto));
    }

    /** MAQOLANI KO'RISHLAR SONINI SANAYDI */
    @PostMapping("/viewsCountingTheArticle/{id}")
    public void viewsCountingTheArticle(@PathVariable UUID id){
        articleService.viewsArticle(id);
    }

    /** MAQOLANI NECHA MARTA KO`RILGANINI QAYTARADI */
    @GetMapping("/numberOfViews/{id}")
    public Integer numberOfViews(@PathVariable UUID id) {
        return articleService.numberOfViews(id);
    }

    /** ADMIN TOMONIDAN MAQOLAGA STATUS BERISH */
    @PostMapping(value = "/giveStatus")
    public HttpEntity<?> giveStatus(@CurrentUser User user, @ModelAttribute GiveStatusDto statusDto, @RequestPart(required = false) MultipartFile file) {
        ApiResponse apiResponse = articleService.giveStatus(user, statusDto, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    /** BARCHA MAQOLALARNI OLIB KELISH UCHUN */
    @GetMapping("/all")
    public List<Article> getAll() {
        return articleService.getAll();
    }

    /** ADMIN UCHUN SHU ID LI MAQOLAGA REDACTOR VA REVIEWERLAR TOMONIDAN QANDAY STATUSLAR BERILGANLIGI HAQIDA MA'LUMOT OLIB KELISH UCHUN */
    @GetMapping("/articleInfoForAdmin/{id}")
    public ArticleInfo getArticleInfoForAdmin(@PathVariable UUID id) {
        return articleService.getArticleInfoForAdmin(id);
    }

    /** ADMINGA SHU ID LI MAQOLAGA QANDAY STATUSLAR BERGANLIGI HAQIDA MA'LUMOT OLIB KELISH UCHUN */
    @GetMapping("/getArticleInfoAdmin/{id}")
    public List<ArticleAdminInfo> getArticleInfoAdmin(@PathVariable UUID id){
        return articleService.getArticleInfoAdmin(id);
    }

    /** MAQOLANI ID SI BO`YICHA OLIB KELISH */
    @GetMapping("/getById/{id}")
    public Article getById(@PathVariable UUID id) {
        return articleService.getById(id);
    }


    /** ADMINSTRATOR TOMONIDAN MAQOLANI NARXI O`ZGARTIRILGANDA MUALLIFGA SMS JO`NATISH */
    @PostMapping("/sendSmsUserPrice")
    public HttpEntity<?> sendSmsUserPrice(@CurrentUser User user, @RequestBody SendSmsUserPriceDto sendSmsUserPrice) {
        ApiResponse apiResponse = articleService.sendSmsUserPrice(user, sendSmsUserPrice);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    /**
     * MENING NASHR ETILGAN MAQOLALARIM
     */
    @GetMapping("/myPublishedArticles")
    public List<Article> getMyPublishedArticles(@CurrentUser User user) {
        return articleService.getMyPublishedArticles(user);
    }


    /**
     * MENING QAYTA ISHLOVGA BERILGAN MAQOLALARIM
     */
    @GetMapping("/myCanceledArticles")
    public List<Article> getMyCanceledArticles(@CurrentUser User user) {
        return articleService.getMyCanceledArticles(user);
    }

    /**
     * MENING NASHR ETILAYOTGAN MAQOLALARIM
     */
    @GetMapping("/myPreparedForPublicationArticles")
    public List<Article> getMyPreparedForPublicationArticles(@CurrentUser User user) {
        return articleService.getMyPreparedForPublicationArticles(user);
    }

    /**
     * MENING TEKSHIRILAYOTGAN MAQOLALARIM
     */
    @GetMapping("/myCheckingArticles")
    public List<Article> getMyCheckingArticles(@CurrentUser User user) {
        return articleService.getMyCheckingArticles(user);
    }

    /**
     * MENING RAD ETILGAN MAQOLALARIM
     */
    @GetMapping("/myRejectedArticles")
    public List<Article> getMyRejectedArticles(@CurrentUser User user) {
        return articleService.getMyRejectedArticles(user);
    }

    /** REDACTORLAR UCHUN SHU ID LI MAQOLAGA REVIEWERLAR TOMONIDAN QANDAY STATUSLAR BERILGANLI HAQIDA MA'LUMOT OLIB KELISH UCHUN */
    @GetMapping("/getArticleInfoForRedactor/{id}")
    public List<ArticleAdminInfo> getArticleInfoForRedactor(@PathVariable UUID id) {
        return articleService.getArticleInfoForRedactor(id);
    }


    /**  */
    @PostMapping("/redactorArticle")
    public ApiResponse redactorArticle(@CurrentUser User user, @RequestBody ArticleRedactorDto redactorDto) {
        return articleService.redactorArticle(user, redactorDto);
    }


    /** ADMINSTRATOR TOMONIDAN MAQOLALARGA STATUS BERISH UCHUN */
    @PostMapping("/articleStatusAdministrator")
    public ApiResponse articleStatusAdministrator(@CurrentUser User user, @RequestBody GiveStatusDto statusDto) {
        return articleService.articleStatusAdministrator(user, statusDto);
    }

    /** REVIEWERLAR VA REDACTORLAR UCHUN ULAR TEKSHIRGAN BARCHA MAQOLALARNI OLIB KELISH UCHUN */
    @GetMapping("/myOldArticles")
    public List<ArticlesForReviewers> myOldArticles(@CurrentUser User user){
        return articleService.getMyOldArticles(user);
    }

    /** ADMINGA REVIEWERLAR VA REDACTORLAR  TEKSHIRGAN BARCHA MAQOLALARNI OLIB KELISH UCHUN */
    @GetMapping("/reviewerCheckedArticles/{id}")
    public HttpEntity<?> reviewerCheckedArticles(@PathVariable UUID id){
        ApiResponse apiResponse = articleService.reviewerCheckedArticles(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**USERLAR UCHUN BARCHA MUALLIFLIK MAQOLALARINI OLIB KELISH*/
    @PostMapping("/allMyArticles/{status}")
    public List<Article> allMyArticles(@CurrentUser User user, @PathVariable String status){
        return articleService.allMyArticles(user,status);
    }


}


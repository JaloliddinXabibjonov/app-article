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

    //        @PostMapping(value = "/addArticle")
//    public HttpEntity<ApiResponse> save(@RequestParam(required = false) Integer sahifaSoni,@RequestParam(required = false) Double price,@RequestParam(required = false) Integer jurnaldaChopEtishSoni, @RequestParam(required = false) Integer bosmaJurnalSoni, @RequestParam(required = false) Integer sertifikatSoni, @RequestParam Boolean doi, @RequestParam(required = false) String description, @RequestParam String[] author, @RequestParam String titleArticle, @RequestParam Integer categoryId, @RequestParam boolean publicOrPrivate, @RequestParam UUID userId, @RequestPart MultipartFile file) throws IOException {
//        ApiResponse apiResponse= articleService.addArticle(sahifaSoni,price,jurnaldaChopEtishSoni, bosmaJurnalSoni, sertifikatSoni, doi,description, author, titleArticle, categoryId, publicOrPrivate, userId, file);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
//    }
    @PostMapping(value = "/addArticle")
    public HttpEntity<ApiResponse> save(@ModelAttribute AddArticleDto dto, @CurrentUser User user, @RequestPart MultipartFile file) throws IOException {
        ApiResponse apiResponse = articleService.addArticle(dto, user, file);
        System.out.println("------" + dto.getAuthorsList().toString());
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/getMyArticle")
    public HttpEntity<?> getMyArticle(@CurrentUser User user) {
        return ResponseEntity.ok(articleService.getMyArticle(user));
    }

    // adminstratorlar uchun articlelarni statusiga qarab get qilish
    @PostMapping("/newMyArticle")
    public ApiResponse newMyArticle(@CurrentUser User user, @RequestBody ArticleStatusInAdmins articleStatusInAdmins) {
        return articleService.newMyArticle(user, articleStatusInAdmins);
    }

    //MAQOLANI ID SI ORQALI ORQALI O`CHIRISH
    @DeleteMapping("/deleteArticle/{id}")
    public HttpEntity<?> deleteMyArticle(@PathVariable UUID id) {
        ApiResponse apiResponse = articleService.deleteArticle(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/articleAddEditors")
    public HttpEntity<ApiResponse> sendNotification(@CurrentUser User user, @RequestBody NotificationForRedacktors notification) {
        ApiResponse apiResponse = articleService.articleAddEditors(user, notification);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/articleRemoveEditor")
    public HttpEntity<ApiResponse> removeEditor(@CurrentUser User user, @RequestBody NotificationForRedacktors notification) {
        ApiResponse apiResponse = articleService.removeEditor(user, notification);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //MAQOLANI ADMIN TOMONIDAN REDACTOR VA REVIEWER LARGA BIRIKTIRISH
    @PostMapping("/addAndRemoveRedactor")
    public HttpEntity<ApiResponse> addAndRemoveRedactor(@CurrentUser User user, @RequestBody AddRedactorDto addRedactorDto) {
        ApiResponse apiResponse = articleService.addAndRemoveRedactor(user, addRedactorDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //REVIEWER VA REDACTORLAR TOMONIDAN MAQOLALARNI QABUL QILISH
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


    //ADMIN PANELIDAGI TAQSIMLANGAN MAQOLALAR BO`LIMIGA MAQOLALARNI KIMLARGA TAQSIMLANGANINI KO`RSATISH UCHUN
    @GetMapping("/distributed")
    public List<Article> getDistributedArticles(@CurrentUser User user) {
        return articleService.getDistributeds(user);

    }

    /**
     * ADMIN TOMONIDAN MAQOLALARNI BIRIKTIRISH UCHUN REDACTOR VA REVIEWERLAR
     */
    @PostMapping("/getReviewerAndRedactorRandom")
    public ApiResponse getReviewerAndRedactorRandom(@RequestBody GetUsersRoleId getUsersRoleId) {
        return articleService.getReviewerAndRedactorRandom(getUsersRoleId);
    }


    //ADMINGA YANGI QO`SHILGAN MAQOLALARNI OLIB KELISH UCHUN
    @GetMapping("/getNewAllArticle")
    public ApiResponse getNewOllArticle() {
        return articleService.getNewOllArticle();
    }

    //ADMINGA NECHTA YANGI MAQOLA QO`SHILGANLIGINI QAYTARADI
    @GetMapping("/newArticlesNumber")
    public HttpEntity<?> newArticles() {
        Integer integer = articleService.getNumberNewArticles();
        return ResponseEntity.ok(integer);
    }

    /**
     * USERGA BIRIKTIRILGAN MAQOLALARNI OLIB KELISH UCHUN
     */
    @PostMapping("/myDuties")
    public HttpEntity<?> myDuties(@CurrentUser User user) {
        ApiResponse apiResponse = articleService.myDuties(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //REDACTOR VA REVIEWERLARGA BIRIKTIRILGAN YANGI MAQOLALARNI OLIB KELISH UCHUN
    @PostMapping("/myNewArticles")
    public HttpEntity<?> myNewArticles(@CurrentUser User user) {
        return ResponseEntity.ok(articleService.myNewArticles(user));
    }

    @GetMapping("/getMyTasks")
    public List<MyTasksDto> getMyTasks(@CurrentUser User user) {
        return articleService.getMyTasks(user);
    }

    @PostMapping("/givenStatus")
    public HttpEntity<?> statusesGivenToTheArticleByTheEditors(@RequestParam UUID userId, @RequestParam(required = false) String description, @RequestParam UUID articleId, @RequestParam String status, @RequestPart(required = false) MultipartFile file) {
        ApiResponse apiResponse = articleService.statusesGivenToTheArticleByTheEditors(userId, description, articleId, status, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/articleInformation")
    public ApiResponse articleInformation(@RequestBody ReviewerAndRedactorResponseDto responseDto) {
        return new ApiResponse("ok", true, articleService.informationArticle(responseDto));
    }

    @GetMapping("/numberOfViews/{id}")
    public Integer numberOfViews(@PathVariable UUID id) {
        return articleService.numberOfViews(id);
    }

    @PostMapping(value = "/giveStatus")
    public HttpEntity<?> giveStatus(@CurrentUser User user, @ModelAttribute GiveStatusDto statusDto, @RequestPart(required = false) MultipartFile file) {
        ApiResponse apiResponse = articleService.giveStatus(user, statusDto, file);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping("/all")
    public List<Article> getAll() {
        return articleService.getAll();
    }

    @GetMapping("/articleInfoForAdmin/{id}")
    public ArticleInfo getArticleInfoForAdmin(@PathVariable UUID id) {
        return articleService.getArticleInfoForAdmin(id);
    }

    @GetMapping("/getArticleInfoAdmin/{id}")
    public List<ArticleAdminInfo> getArticleInfoAdmin(@PathVariable UUID id){
        return articleService.getArticleInfoAdmin(id);
    }

    @GetMapping("/getById/{id}")
    public Article getById(@PathVariable UUID id) {
        return articleService.getById(id);
    }

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

    /**
     * MENING BARCHA MAQOLALARIM(MUALLIFLIK MAQOLALARIM)
     */
    @GetMapping("/myCopyrightedArticles")
    public List<Article> getMyCopyrightedArticles(@CurrentUser User user) {
        return articleService.getMyCopyRightedArticles(user);
    }
    @GetMapping("/getArticleInfoForRedactor/{id}")
    public List<ArticleAdminInfo> getArticleInfoForRedactor(@PathVariable UUID id) {
        return articleService.getArticleInfoForRedactor(id);
    }


    @PostMapping("/redactorArticle")
    public ApiResponse redactorArticle(@CurrentUser User user, @RequestBody ArticleRedactorDto redactorDto) {
        articleService.redactorArticle(user, redactorDto);
        return new ApiResponse("ok ", true);
    }


    @PostMapping("/articleStatusAdministrator")
    public ApiResponse articleStatusAdministrator(@CurrentUser User user, @RequestBody GiveStatusDto statusDto) {
        articleService.articleStatusAdministrator(user, statusDto);
        return new ApiResponse("ok ", true);
    }


}


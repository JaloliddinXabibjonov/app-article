package com.example.article.controller;

import com.example.article.entity.Article;
import com.example.article.entity.User;
import com.example.article.entity.UserStorage;
import com.example.article.payload.*;
import com.example.article.repository.UserRepository;
import com.example.article.secret.CurrentUser;
import com.example.article.servise.UserService;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    @GetMapping("/fetchAllUser")
    public Set<String> fetchAll() {
        return UserStorage.getInstance().getUsers();
    }


    @PostMapping("/register")
    public HttpEntity<?> userAdd(@RequestBody SignUp signUp) {
        return ResponseEntity.ok(userService.register(signUp));
    }

    @PostMapping("/registerAdmin")
    public HttpEntity<?> registerAdmin(@RequestBody SignUp signUp) {
        return ResponseEntity.ok(userService.registerAdmin(signUp));
    }


    /**
     * REVIEWERLARNI ADMIN TOMONIDAN RO`YXATDAN O`TKAZISH
     */
    @SneakyThrows
    @PostMapping(value = "/registerReviewer")
    public HttpEntity<?> registerReviewer(@RequestParam String lastName, @RequestParam String firstName, @RequestParam String fathersName, @RequestParam String phoneNumber, @RequestParam String password, @RequestParam String email, @RequestParam Set<Integer> categoryIdList,
                                          @RequestParam String workPlace, @RequestParam String workExperience, @RequestPart MultipartFile file, @RequestParam String academicDegree, @RequestParam List<Integer> languages, @RequestPart MultipartFile passport) {
        ApiResponse apiResponse = userService.registerReviewer(lastName, firstName, fathersName, phoneNumber, password, email, categoryIdList, workPlace, workExperience, file, academicDegree, languages, passport);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    // bu userni to'liq ma'lumotlarini kiritish uchun
    @PostMapping("/buildProfile")
    public HttpEntity<ApiResponse> buildProfile(@CurrentUser User user, @RequestBody SignUp signUp) {
        ApiResponse apiResponse = userService.buildProfile(user, signUp);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }


    @PostMapping("/edit")
    public HttpEntity<?> edit(@CurrentUser User user, @RequestBody SignUp signUp) {
        ApiResponse apiResponse = userService.edit(user, signUp);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    /**
     * ADMIN TOMONIDAN USERLARNI EDIT QILISH
     */
    @PostMapping("/editUserFromAdmin")
    public HttpEntity<ApiResponse> editUserFromAdmin(@CurrentUser User currentUser, @RequestBody SignUp userDto) {
        ApiResponse apiResponse = userService.editUserFromAdmin(currentUser, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody SignIn signIn) {
        return ResponseEntity.ok(userService.login(signIn));
    }


    /**
     * USERLARNI ADMIN TOMONIDAN O'CHIRIB TASHLASH
     */
    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = userService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/me")
    public HttpEntity<?> me(@CurrentUser User user) {
        User user1 = userRepository.findById(user.getId()).get();
        System.out.println("    ---" + user);
        return ResponseEntity.status(user != null ? 200 : 409).body(user1);
    }

    /**
     * ADMIN TOMONIDAN TIZIMGA XODIM QO`SHISH
     */
    @PostMapping("/addEmployee")
    public HttpEntity<ApiResponse> addEmployee(@RequestBody UserDto userDto) throws ExecutionException, IllegalAccessException {
        ApiResponse apiResponse = userService.addEmployee(userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

//    @PostMapping("/changeUserActive/{id}")
//    public ApiResponse changeUserActive(@PathVariable UUID id, @CurrentUser User user, @RequestBody boolean status) {
//        return new ApiResponse("change", true, userService.changeUserActive(id, status, user));
//    }
//
//    @PostMapping("/searchUser")
//    public HttpEntity<?> searchUsers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
//                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
//                                     @RequestParam(value = "search", defaultValue = "777") String search,
//                                     @RequestParam(value = "roles_id" ) Integer roles_id,
//                                     @RequestParam(value = "enabled",defaultValue = "true") boolean enabled,
//                                     @RequestParam(value = "categoryId") Integer categoryId
//    ) throws IllegalAccessException {
//        return ResponseEntity.ok(userService.searchUser(search, roles_id, enabled, categoryId, page, size));
//    }

//    @PostMapping("/searchUser")
//    public HttpEntity<?> searchUsers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
//                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
//                                     @RequestParam(value = "search", defaultValue = "777") String search,
//                                     @RequestParam(value = "roles_id") Integer roles_id,
//                                     @RequestParam(value = "enabled", defaultValue = "true") boolean enabled,
//                                     @RequestParam(value = "categoryId") Integer categoryId
//    ) throws IllegalAccessException {
//        return ResponseEntity.ok(userService.searchUser(search, roles_id, enabled, categoryId, page, size));
//    }


    /**
     * ADMIN TOMONIDAN TIZIM FOYDALANUVCHILARINI QIDIRISH
     */
    @PostMapping("/search")
    public HttpEntity<?> search(@RequestBody SearchUser searchUser) throws IllegalAccessException {
        ApiResponse apiResponse = userService.search(searchUser);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * YANGI QO`SHILGAN REVIEWERLARNI OLIB KELISH
     */
    @GetMapping("/getNewReviewer")
    public ApiResponse getNewReviewer() {
        return new ApiResponse("Yangi qo'shilgan reviewerlar", true, userService.getNewReviewer());
    }

    /**
     * TIZIMGA YANGI QO`SHILGAN REVIEWERLAR SONINI OLIB KELISH
     */
    @GetMapping("/getNewReviewerCount")
    public Integer getNewReviewerCount() {
        return userService.getNewReviewerCount();
    }

    /**
     * USERNI TIZIMDA ISHLASHI UCHUN RUXSAT BERISH
     */
    @PostMapping("/acceptedUser")
    public HttpEntity<?> acceptedUser(@CurrentUser User user, @RequestBody ReviewerDto reviewerDto) {
        ApiResponse apiResponse = userService.acceptedUser(user, reviewerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * MA'LUM VAQT ORALIG'IDAGI RO`YXATDAN O`TGAN USERLARNI OLIB KELISH UCHUN
     */
    @GetMapping("/numberOfRegistredUsersBetween")
    public Integer numberOfUsersBetween(@RequestParam Long start, @RequestParam Long end) {
        return userService.numberOfRegistredUsers(start, end);
    }

    /**
     * DASHBOARD UCHUN
     */
    @GetMapping("/dashboard")
    public HttpEntity<?> dashboard() {
        return ResponseEntity.ok(userService.dashboard());
    }

    /**
     * adminstratorlar defoult deadline beradi
     */
    @PostMapping("/defaultDeadlineAddAdministrator")
    public HttpEntity<?> defaultDeadlineAddAdministrator(@RequestBody DeadlineAdministratorDto defaultDeadlineAddAdministrator) {
        ApiResponse apiResponse = userService.defaultDeadlineAddAdministrator(defaultDeadlineAddAdministrator);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    /**
     * USERNI BERILGAN VAQT ORALIG'IDAGI QABUL QILGAN MAQOLALARI
     */
    @GetMapping("/getAcceptedArticlesByUsersBetween")
    public List<Article> getAcceptedArticlesByUsersBetween(@RequestBody TimeBetween timeBetween) {
        return userService.getAcceptedArticlesByUsersBetween(timeBetween);
    }

    /**
     * USERNING MAQOLALAR BO`YICHA STATISTIKASI
     */
    @GetMapping("/statisticsArticlesForUsers/{id}")
    public StatisticsArticlesForUsersDto getStatisticsArticlesForUsers(@PathVariable UUID id) {
        return userService.getStatisticsArticlesForUsers(id);
    }

    /**
     * USERNI ID SI BO`YICHA GET QILISH
     */
    @GetMapping("getById/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = userService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * RO`YXATDAN O`TAYOTGAN ROLE_USER GA MUALLIFLIK KODINI TAKRORLANMAS HOLDA GENERATSIYA QILIB BERISH
     */
    @GetMapping("/code")
    public Integer generator() {
        return userService.generatorCode();
    }

    @SneakyThrows
    @PostMapping("/firebase")
    public void createCrud(@RequestBody SignIn signIn) {
        userService.createCRUD(signIn);
    }

//    @GetMapping("/getMyNotifications")
//    public List<Notifications> getMyNotifications(@CurrentUser User user){
//        return userService.getMyNotifications(user);
//    }

    /**
     * MUALLIFNI MUALLIFLIK KODI ORQALI OLIB KELISH
     */
    @GetMapping("/getAuthorByCode/{code}")
    public HttpEntity<ApiResponse> getAuthorByCode(@PathVariable Integer code) {
        ApiResponse apiResponse = userService.getAuthorByCode(code);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 201).body(apiResponse);
    }

    /**
     * PAROL TIKLASH uchun tasdiqlash kodi yuborish
     */
    @PostMapping("/createNewPassword/{phoneNumber}")
    public ApiResponse createNewPassword(@PathVariable String phoneNumber) {
        return userService.createNewPassword(phoneNumber);
    }

    /**
     * YUBORILGAN KODNI TASDIQLASH
     */
    @PostMapping("/verifyCode")
    public HttpEntity<ApiResponse> verifyCode(@CurrentUser User user, @RequestBody VerifyCode verifyCode) {
        ApiResponse apiResponse = userService.verifyCode(user, verifyCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    /** PAROL TIKLASH */
//    @PutMapping("/editPassword/{password}")
//    public HttpEntity<ApiResponse> editNewPassword(@CurrentUser User user, @PathVariable String password){
//        ApiResponse apiResponse=userService.editPassword(user,password);
//        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
//    }

    /**
     * PROFILGA RASM QO`YISH
     */
    @PostMapping("/addPhoto")
    public HttpEntity<ApiResponse> addPhoto(@CurrentUser User user, @RequestPart MultipartFile file) {
        return ResponseEntity.status(userService.addPhoto(user, file).isSuccess() ? 200 : 409).body(userService.addPhoto(user, file));
    }

    /**
     * Barcha yangi  reviewerlarni olib kelish
     */
    @GetMapping("/allNewReviewers")
    public List<User> allNewReviewers() {
        return userService.allNewReviewers();
    }

    @PostMapping("/activeEdite/{id}")
    public ApiResponse activeEdite(@PathVariable UUID id) {
        return userService.activeEdite(id);
    }


}

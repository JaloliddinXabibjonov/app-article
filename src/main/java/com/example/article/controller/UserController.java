package com.example.article.controller;

import com.example.article.entity.Article;
import com.example.article.entity.User;
import com.example.article.entity.UserStorage;
import com.example.article.payload.*;
import com.example.article.repository.UserRepository;
import com.example.article.secret.CurrentUser;
import com.example.article.servise.UserService;
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


    @GetMapping("/registration/{userName}")
    public ResponseEntity<Void> registration(@PathVariable String userName) {
        System.out.println("handling register user request : " + userName);

        try {
            UserStorage.getInstance().setUser(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return null;
    }


    @GetMapping("/fetchAllUser")
    public Set<String> fetchAll() {
        return UserStorage.getInstance().getUsers();
    }


    @PostMapping("/register")
    public HttpEntity<?> userAdd(@RequestBody SignUp signUp) {
        return ResponseEntity.ok(userService.register(signUp));
    }

    @SneakyThrows
    @PostMapping(value = "/registerReviewer")
    public HttpEntity<?> registerReviewer(@RequestParam String lastName, @RequestParam String firstName, @RequestParam String fathersName, @RequestParam String phoneNumber, @RequestParam String password, @RequestParam String email, @RequestParam Set<Integer> categoryIdList,
                                          @RequestParam String workPlace, @RequestParam String workExperience, @RequestPart MultipartFile file, @RequestParam String academicDegree, @RequestParam String languages, @RequestPart MultipartFile passport) {
        System.out.println(" register " + lastName + " " + firstName + " " + fathersName + " " + Double.valueOf(workExperience));
        ApiResponse apiResponse = userService.registerReviewer(lastName, firstName, fathersName, phoneNumber, password, email, categoryIdList, workPlace, workExperience, file, academicDegree, languages, passport);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    // bu userni to'liq ma'lumotlarini kiritish uchun
    @PostMapping("/buildProfile")
    public HttpEntity<ApiResponse> buildProfile(@CurrentUser User user, @RequestBody SignUp signUp) {
        ApiResponse apiResponse = userService.buildProfile(user, signUp);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }


    @PostMapping("/editUserFromAdmin")
    public HttpEntity<ApiResponse> editUserFromAdmin(@CurrentUser User currentUser, @RequestBody SignUp userDto) {
        ApiResponse apiResponse = userService.editUserFromAdmin(currentUser, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @PostMapping("/edit")
    public HttpEntity<?> edit(@CurrentUser User user, @RequestBody SignUp signUp) {
        ApiResponse apiResponse = userService.edit(user, signUp);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody SignIn signIn) {
        return ResponseEntity.ok(userService.login(signIn));
    }


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


    @PostMapping("/search")
    public HttpEntity<?> search(@RequestBody SearchUser searchUser) throws IllegalAccessException {
        ApiResponse apiResponse = userService.search(searchUser);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/getNewReviewer")
    public ApiResponse getNewReviewer() {
        return new ApiResponse("Yangi qo'shilgan reviewerlar", true, userService.getNewReviewer());
    }

    /***/
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
     * method adminstratorlar defoult deadline beradi
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

    @GetMapping("getById/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = userService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/code")
    public Integer generator() {
        return userService.generatorCode();
    }

    @SneakyThrows
    @PostMapping("/firebase")
    public void createCrud(@RequestBody SignIn signIn){
        userService.createCRUD(signIn);
    }

//    @GetMapping("/getMyNotifications")
//    public List<Notifications> getMyNotifications(@CurrentUser User user){
//        return userService.getMyNotifications(user);
//    }
}

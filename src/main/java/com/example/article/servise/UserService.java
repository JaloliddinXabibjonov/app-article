package com.example.article.servise;


import com.example.article.entity.*;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.RoleName;
import com.example.article.entity.enums.UserStatus;
import com.example.article.entity.enums.Watdou;
import com.example.article.payload.*;
import com.example.article.repository.*;
import com.example.article.secret.JwtProvider;
import com.example.article.utils.AppConstants;
import com.example.article.utils.CommonUtills;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AttachmentService attachmentService;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Autowired
//    PushNotificationService pushNotificationService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    InformationArticleRepository informationArticleRepository;


    @Autowired
    InformationUserRepository informationUserRepository;


    @Autowired
    DeadlineAdministratorRepository deadlineAdministratorRepository;


    public String register(SignUp signUp) {
        Optional<User> userOptional = userRepository.findByPhoneNumberAndDeleteFalse(signUp.getPhoneNumber());
        if (userOptional.isEmpty()) {
            User user = new User();
            System.out.println("telefon   " + signUp.getPhoneNumber());
            user.setPhoneNumber(signUp.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRoles(roleRepository.findAllByRoleNameIn(singleton(RoleName.ROLE_USER.name())));
            user.setEnabled(false);
            userRepository.save(user);
            System.out.println(jwtProvider.generateJwtToken(user));
            return (jwtProvider.generateJwtToken(user));
        }
        return ("User is already exist");
    }

    public ApiResponse registerReviewer(String lastName, String firstName, String fathersName, String phoneNumber, String password, String email, Set<Integer> categoryIdList,
                                        String workPlace, String workExperience, MultipartFile scientificWork, String academicDegree, String languages, MultipartFile passport) {
        boolean exists = userRepository.existsByPhoneNumberAndDeleteFalse(phoneNumber);
        if (exists)
            return new ApiResponse("Bunday telefon raqam orqali avval ro`yxatdan o`tilgan", false);
        try {
            User user = new User();
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setFatherName(fathersName);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setCategories(categoryRepository.findAllByIdIn(categoryIdList));
            user.setWorkPlace(workPlace);
            user.setWorkExperience(workExperience);
            user.setScientificWork(singletonList(attachmentService.upload1(scientificWork)));
            user.setAcademicDegree(academicDegree);
            user.setLanguages(languages);
            user.setRoles(singletonList(roleRepository.getById(3)));
            user.setPhotos(singletonList(attachmentService.upload1(passport)));
            user.setEnabled(true);
            user.setActive(false);
            userRepository.save(user);
            return new ApiResponse("Muvaffaqiyatli ro`yxatdan o`tdingiz. Akkountingiz tekshirish uchun adminstratorga yuborildi. Iltimos akkountingiz aktivlashtirilishini kuting", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi, iltimos qaytadan urinib ko`ring", false);
        }
    }

    // bu userni to'liq ma'lumotlarini kiritish uchun
    public ApiResponse buildProfile(User user, SignUp signUp) {
        user.setLastName(signUp.getLastName());
        user.setFirstName(signUp.getFirstName());
        user.setFatherName(signUp.getFatherName());
        user.setEmail(signUp.getEmail());
//                    user.setPhotos(Collections.singletonList(attachmentRepository.getById(signUp.getPhotoId())));
        userRepository.save(user);
        return new ApiResponse("Saved", true);
    }

    /**ADMIN TOMONIDAN YANGI USER QO`SHISH*/
    public ApiResponse addEmployee(UserDto userDto) {
        try {
            boolean exists = userRepository.existsByPhoneNumber(userDto.getPhoneNumber());
            if (exists)
                return new ApiResponse("Bunday telefon raqam tizimda mavjud!!!", false);
            User user = new User();
            List<Category> categories=new ArrayList<>();
            for (Integer integer : userDto.getCategoryId()) {
                categories.add(categoryRepository.findById(integer).get());
            }
            user.setCategories(categories);
            user.setLastName(userDto.getLastName());
            user.setFirstName(userDto.getFirstName());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setEnabled(true);
            user.setEmail(userDto.getEmail());
            user.setLanguages(userDto.getLanguages());
            user.setRoles(singletonList(roleRepository.getById(userDto.getRoleId())));
            userRepository.save(user);
            return new ApiResponse("Saqlandi", true);
        }
        catch(Exception e){
            return new ApiResponse("Xatolik yuz berdi, birozdan keyin qayta urinib ko'ring",false);
        }
}


    // user rasm qoyish uchun
    public ApiResponse userAddPhoto(UUID userId, MultipartFile file) {
        try {
            User user = userRepository.getById(userId);
            user.setPhotos(Collections.singletonList(attachmentService.upload1(file)));
            userRepository.save(user);
            return new ApiResponse(" Photo saved ", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(" Not save photo");
        }
    }


    public String login(SignIn signIn) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getPassword(), signIn.getPhoneNumber())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User principal = (User) authenticate.getPrincipal();
        return jwtProvider.generateJwtToken(principal);
    }

    public ApiResponse edit(User user, SignUp signUp) {
        try {
            user.setLastName(signUp.getLastName().equals("") ? user.getLastName() : signUp.getLastName());
            user.setFirstName(signUp.getFirstName().equals("") ? user.getFirstName() : signUp.getFirstName());
            if (!signUp.getPassword().equals(""))
                user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setPhoneNumber(signUp.getPhoneNumber().equals("") ? user.getPhoneNumber() : signUp.getPhoneNumber());
            user.setFatherName(signUp.getFatherName().equals("") ? user.getFatherName() : signUp.getFatherName());
            user.setEmail(signUp.getEmail().equals("") ? user.getEmail() : signUp.getEmail());
            user.setLanguages(signUp.getLanguages().equals("") ? user.getLanguages() : signUp.getLanguages());
            user.setAcademicDegree(signUp.getAcademicDegree().equals("") ? user.getAcademicDegree() : signUp.getAcademicDegree());
            user.setWorkExperience(signUp.getWorkExperience().equals("") ? user.getWorkExperience() : signUp.getWorkExperience());
            user.setWorkPlace(signUp.getWorkPlace().equals("") ? user.getWorkPlace() : signUp.getWorkPlace());

            InformationUser informationUser = new InformationUser();
            informationUser.setAcceptedUser(user);
            informationUser.setWhenAndWho(new Date());
            informationUser.setUserStatus(UserStatus.EDIT);
            informationUser.setRedactorAndReviewer(user);
            informationUserRepository.save(informationUser);
            userRepository.save(user);

            System.out.println("----->>" + signUp.getPassword());
            System.out.println("----->>" + signUp.getPhoneNumber());
            System.out.println("----->>" + signUp.getFirstName());
            System.out.println("----->>" + signUp.getLastName());
            System.out.println("----->>" + signUp.getFatherName());
            System.out.println("----->>" + signUp.getWorkExperience());
            System.out.println("----->>" + signUp.getEmail());
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    @SneakyThrows
    public ApiResponse editUserFromAdmin(User currentUser, SignUp signUp) {
        try {
            User user = new User();
            boolean exists1 = userRepository.existsByPhoneNumber(signUp.getPhoneNumber());
            if (exists1)
            if (signUp.getUserId() != null) {
                user = userRepository.getById(signUp.getUserId());
            }
            if (!signUp.getPassword().equals(""))
                user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setEmail(signUp.getEmail().equals("") ? user.getEmail() : signUp.getEmail());
            user.setLastName(signUp.getLastName().equals("") ? user.getLastName() : signUp.getLastName());
            user.setFirstName(signUp.getFirstName().equals("") ? user.getFirstName() : signUp.getFirstName());
            user.setFatherName(signUp.getFatherName().equals("") ? user.getFatherName() : signUp.getFatherName());
            user.setLanguages(signUp.getLanguages().equals("") ? user.getLanguages() : signUp.getLanguages());
            user.setPhoneNumber(signUp.getPhoneNumber().equals("") ? user.getPhoneNumber() : signUp.getPhoneNumber());
            user.setAcademicDegree(signUp.getAcademicDegree().equals("") ? user.getAcademicDegree() : signUp.getAcademicDegree());
            user.setWorkExperience(signUp.getWorkExperience().equals("") ? user.getWorkExperience() : signUp.getWorkExperience());
            user.setWorkPlace(signUp.getWorkPlace().equals("") ? user.getWorkPlace() : signUp.getWorkPlace());

            InformationUser informationUser = new InformationUser();
            informationUser.setAcceptedUser(currentUser);
            informationUser.setWhenAndWho(new Date());
            informationUser.setUserStatus(UserStatus.EDIT);
            informationUser.setRedactorAndReviewer(userRepository.getById(signUp.getUserId()));
            informationUserRepository.save(informationUser);

            userRepository.save(user);
            return new ApiResponse("Successfully edited", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    public ApiResponse delete(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDelete(true);
            userRepository.save(user);
            return new ApiResponse("Successfully deleted", true);
        }
        return new ApiResponse("Error", false);
    }

//    public ApiResponse searchUser(String search, Integer roles_id, boolean enabled, Integer categoryId, Integer page, Integer size)
//            throws IllegalAccessException {
//        Page<User> users = null;
//        System.out.println(search);
//
//// search ishlidi
//        if (roles_id == 10 && categoryId == null && !search.equals("777")) {
//            users = userRepository.findAllByEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase
//                    (enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));
//        } else if
//            //  role ishlidi
//        (categoryId == null && search.equals("777") && roles_id != null) {
//            users = userRepository.findAllByEnabledAndRolesId(enabled, roles_id, CommonUtills.simplePageable(page, size));
//
////             categorya ishlidi
//        } else if (roles_id == null && search.equals("777") && categoryId != null) {
//            users = userRepository.findAllByEnabledAndCategoriesIdIn(enabled, singleton(categoryId), CommonUtills.simplePageable(page, size));
//
//            // categorya bn role ishlidi
//        } else if (roles_id != null && categoryId != null && search.equals("777")) {
//            users = userRepository.findAllByEnabledAndCategoriesIdInAndRolesId(enabled, singleton(categoryId), roles_id, CommonUtills.simplePageable(page, size));
//
//            // categorya va role search ham ishlidi
//        } else if (roles_id != null && categoryId != null && !search.equals("777")) {
//            users = userRepository.findAllByRolesIdAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(roles_id, Collections.singleton(categoryId), enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));
//
//            // role bn search
//        } else if (categoryId == null && roles_id != null && !search.equals("777")) {
//
//            users = userRepository.findAllByRolesIdAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(roles_id, enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));
//
//            //  category bn search
//        } else if (roles_id == null && categoryId != null && !search.equals("777")) {
//            users = userRepository.findAllByCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(Collections.singleton((categoryId)), enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));
//        }
//        return new ApiResponse("All user ", true, users);
//    }


    public ApiResponse search(SearchUser searchUser) throws IllegalAccessException {

        Page<User> users = userRepository.findAllByDeleteFalse(CommonUtills.simplePageable(AppConstants.DEFAULT_PAGE_NUMBER1, AppConstants.DEFAULT_PAGE_SIZE1));
//
//        System.out.println(" categor id  " + searchUser.getCategoryId());
//        System.out.println(" role id  " + searchUser.getRoles_id());
//        System.out.println(" search uchun    " + searchUser.getSearch());
//        System.out.println(" enabled  " + searchUser.isEnabled());

        // search ishlidi
        if (searchUser.getRoles_id() == null && searchUser.getCategoryId() == null && !searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByEnabledAndFirstNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndLastNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndFatherNameContainingIgnoringCaseAndDeleteFalseOrEnabledAndEmailContainingIgnoringCaseAndDeleteFalseOrEnabledAndPhoneNumberContainingIgnoringCase(
                    searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("search ishladi");
        }
        //  role ishlidi
        else if (searchUser.getCategoryId() == null && searchUser.getSearch().equals("777") && searchUser.getRoles_id() != null) {
            System.out.println(searchUser.getRoles_id() + "-------" + searchUser.isEnabled());
            users = userRepository.findAllByEnabledAndRolesIdAndDeleteFalse(searchUser.isEnabled(), searchUser.getRoles_id(), false, CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));

            System.out.println("role ishladi  ");
            return new ApiResponse(" role  ", true, users);
        }
        //categorya ishlidi
        else if (searchUser.getRoles_id() == null && searchUser.getSearch().equals("777") && searchUser.getCategoryId() != null) {
            users = userRepository.findAllByEnabledAndDeleteFalseAndCategoriesIdIn(searchUser.isEnabled(), singleton(searchUser.getCategoryId()), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("categor ishladi  ");
        }
        // categorya bn role ishlidi
        else if (searchUser.getRoles_id() != null && searchUser.getCategoryId() != null && searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByEnabledAndDeleteFalseAndCategoriesIdInAndRolesId(searchUser.isEnabled(), singleton(searchUser.getCategoryId()), searchUser.getRoles_id(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            for (User user : users) {
                System.out.println(user.getFirstName() + " " + user.getCategories().get(0).getName());
            }

            System.out.println("category bn role ishladi  ");
        }
        // categorya va role search ham ishlidi
        else if (searchUser.getRoles_id() != null && searchUser.getCategoryId() != null && !searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("category bn role va search  ishladi  ");
        }
        // role bn search
        else if (searchUser.getCategoryId() == null && searchUser.getRoles_id() != null && !searchUser.getSearch().equals("777")) {
            System.out.println("search bn role ishladi  ");
            users = userRepository.findAllByRolesIdAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            return new ApiResponse(" role va searech ", true, users);
        }
        //  category bn search
        else if (searchUser.getRoles_id() == null && searchUser.getCategoryId() != null && !searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByCategoriesIdInAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));

            for (User user : users) {
                System.out.println(user.getFirstName() + " " + user.getCategories().get(0).getName());
            }
            System.out.println("category bn search ishladi");
        }
        return new ApiResponse("All users: ", true, users);
    }


    /**
     * bu yangi qo'shilgan reviewrlarni get qilib beradi
     */
    public ApiResponse getNewReviewer() {
        List<User> users = userRepository.findAllByEnabledFalseAndActiveFalseAndDeleteFalseAndRolesId(3);
        if (users == null)
            return new ApiResponse("Yangi qo'shilgan reviewrlar yo'q");
        return new ApiResponse("Yangi qo'shilgan reviewrlar ", true, users);
    }

    /**
     * yangi qo'shilgan reviewerlarni soni
     */
    public Integer getNewReviewerCount() {
        return (userRepository.countAllByEnabledFalseAndActiveFalseAndRolesIdAndDeleteFalse(3));
    }

    /**
     * yangi qo'shilgan reviewerlarni activlashtirish
     */
    public ApiResponse acceptedUser(User user, ReviewerDto reviewerDto) {

        Optional<User> optionalUser = userRepository.findById(reviewerDto.getUserId());
        if (optionalUser.isPresent()) {
            User user1 = optionalUser.get();
            if (reviewerDto.isActive())
                user1.setActive(reviewerDto.isActive());
            user1.setEnabled(reviewerDto.isActive());
            userRepository.save(user1);
            informationUserRepository.save(new InformationUser(user, user1, new Date(), UserStatus.ACCEPTED));
            return new ApiResponse(reviewerDto.isActive()?"Foydalanuvchi faollashtirildi":"Foydalanuvchi bloklandi", true);
        }
        return new ApiResponse("Foydalanuvchi topilmadi", false);
    }

    /**
     * MA'LUM VAQT ORALIG'IDAGI RO`YXATDAN O`TGAN USERLAR SONI
     */
    public Integer numberOfRegistredUsers(long startTime, long endTime) {
        Timestamp start = new Timestamp(startTime);
        Timestamp end = new Timestamp(endTime);
        return userRepository.countAllByCreatedAtBetween(start, end);
    }

    /**
     * DASHBOARD UCHUN
     */
    public ForDashboard dashboard() {
//        try {
        ForDashboard forDashboard = new ForDashboard();
        forDashboard.setNumberOfAdmins(userRepository.countAllByRolesId(1));         //adminlar soni
        forDashboard.setNumberOfUsers(userRepository.countAllByRolesId(4));         //userlar soni
        forDashboard.setNumberOfReviewers(userRepository.countAllByRolesId(3));     //reviewerlar soni
        forDashboard.setNumberOfRedactors(userRepository.countAllByRolesId(2));     //redactorlar soni
        forDashboard.setNumberOfNewArticles(articleRepository.countAllByPayTrueAndConfirmFalse());      //tasdiqlanadigan maqolalar soni
        forDashboard.setNumberOfInReviewArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.BEGIN_CHECK));     //tahrirdagi maqolalar soni
        forDashboard.setNumberOfIsBeingEditedArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.PREPARING_FOR_PUBLICATION));      //nashrga tayyorlanayotgan  maqolalar soni
        forDashboard.setNumberOfReadyOfPublicationArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.PREPARED_FOR_PUBLICATION));      //nashrga tayyor maqolalar soni
        forDashboard.setNumberOfFreeAndPublishedArticles(articleRepository.countAllByPublicPrivateAndArticleStatusName(false, ArticleStatusName.PUBLISHED));        //nashr qilingan va bepul maqolalar soni
        forDashboard.setNumberOfPaidAndPublishedArticles(articleRepository.countAllByPublicPrivateAndArticleStatusName(true, ArticleStatusName.PUBLISHED));           //nashr qilingan va pullik maqolalar soni
        forDashboard.setNumberOfNewAndPayFalse(articleRepository.countAllByPayFalse());         //hali puli to`lanmagan maqolalar soni
        forDashboard.setNumberOfRejectedArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.REJECTED));
        forDashboard.setNumberOfRecycleArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.RECYCLE));
        System.out.println(forDashboard);
        return forDashboard;
//        } catch (Exception e) {
//            return new ForDashboard();
//
//        }
    }


    public ApiResponse defaultDeadlineAddAdministrator(DeadlineAdministratorDto administratorDto) {
        DeadlineAdministrator deadlineAdministrator = new DeadlineAdministrator();
        if (administratorDto.getId() != null) {
            deadlineAdministrator = deadlineAdministratorRepository.getById(administratorDto.getId());
        }
        deadlineAdministrator.setAdministratorId(administratorDto.getAdministratorId());
        deadlineAdministrator.setDeadline(administratorDto.getDeadline());
        deadlineAdministratorRepository.save(deadlineAdministrator);
        return new ApiResponse("Saved", true);
    }

    /**
     * USERNI BERILGAN VAQT ORALIG'IDAGI QABUL QILGAN MAQOLALARI
     */
    public List<Article> getAcceptedArticlesByUsersBetween(TimeBetween timeBetween) {
        return informationArticleRepository.findAllByCreatedAtBetweenAndArticleStatusNameAndRedactorId(new Timestamp(timeBetween.getStart()),
                new Timestamp(timeBetween.getEnd()),
                ArticleStatusName.I_ACCEPTED,
                timeBetween.getUserId());
    }

    /**
     * USERNING MAQOLALAR BO`YICHA STATISTIKASI
     */
    public StatisticsArticlesForUsersDto getStatisticsArticlesForUsers(UUID userId) {
        StatisticsArticlesForUsersDto forUsersDto = new StatisticsArticlesForUsersDto();
        forUsersDto.setAccepteds(informationArticleRepository.countAllByArticleStatusNameAndRedactorId(ArticleStatusName.I_ACCEPTED, userId));
        forUsersDto.setDidNotAccepteds(informationArticleRepository.countAllByArticleStatusNameAndRedactorId(ArticleStatusName.I_DID_NOT_ACCEPT, userId));
        forUsersDto.setCheckAndAccepteds(informationArticleRepository.countAllByArticleStatusNameAndRedactorId(ArticleStatusName.CHECK_AND_ACCEPT, userId));
        forUsersDto.setCheckAndCancels(informationArticleRepository.countAllByArticleStatusNameAndRedactorId(ArticleStatusName.CHECK_AND_CANCEL, userId));
        forUsersDto.setCheckAndRecycles(informationArticleRepository.countAllByArticleStatusNameAndRedactorId(ArticleStatusName.CHECK_AND_RECYCLE, userId));
        return forUsersDto;
    }

    public ApiResponse getById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> new ApiResponse("Muvaffaqiyatli bajarildi", true, user)).orElseGet(() -> new ApiResponse("Foydalanuvchi topilmadi", false));
    }
}

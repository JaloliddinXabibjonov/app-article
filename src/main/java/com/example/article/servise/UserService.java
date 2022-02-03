package com.example.article.servise;


import com.example.article.entity.*;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.RoleName;
import com.example.article.entity.enums.UserStatus;
import com.example.article.entity.enums.Watdou;
import com.example.article.payload.*;
import com.example.article.repository.*;
import com.example.article.secret.JwtProvider;
import com.example.article.utils.CommonUtills;
import com.google.protobuf.Api;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
        Optional<User> userOptional = userRepository.findByPhoneNumber(signUp.getPhoneNumber());
        if (userOptional.isEmpty()) {
            User user = new User();
            System.out.println("telefon   " + signUp.getPhoneNumber());
            user.setPhoneNumber(signUp.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRoles(roleRepository.findAllByRoleNameIn(singleton(RoleName.ROLE_USER.name())));
            userRepository.save(user);
            System.out.println(jwtProvider.generateJwtToken(user));
            return (jwtProvider.generateJwtToken(user));
        }
        return ("User is already exist");
    }

    public ApiResponse registerReviewer(String lastName, String firstName, String fathersName, String phoneNumber, String password, String email, Set<Integer> categoryIdList,
                                        String workPlace, String workExperience, MultipartFile scientificWork, String academicDegree, String languages, MultipartFile passport) {
        boolean exists = userRepository.existsByPhoneNumber(phoneNumber);
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
            user.setEnabled(false);
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
        return new ApiResponse("Saved   ", true);
    }

    public ApiResponse saveAndEditUser(UserDto userDto) {
        try {
            Optional<User> userOptional = userRepository.findByPhoneNumber(userDto.getPhoneNumber());
            User user = new User();
            if (userDto.getId() != null) {
                user = userRepository.getById(userDto.getId());
            }
            if (userOptional.isEmpty()) {
                user.setLastName(userDto.getLastName());
                user.setFirstName(userDto.getFirstName());
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                user.setPhoneNumber(userDto.getPhoneNumber());
                user.setEnabled(userDto.isActive());
                user.setRoles(roleRepository.findAllByIdIn(userDto.getRole()));
                user.setCategories(categoryRepository.findAllById(userDto.getCategoryId()));
                userRepository.save(user);
                return new ApiResponse("Saved user ", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiResponse("Error", false);
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
        user.setPassword(signUp.getPassword());
        user.setEmail(signUp.getEmail());
        user.setLastName(signUp.getLastName());
        user.setPhoneNumber(signUp.getPhoneNumber());
//        user.setPhotos(Collections.singletonList(attachmentRepository.getById(signUp.getPhotoId())));
        userRepository.save(user);
        return new ApiResponse("Successfully edited", true);
    }

    public ApiResponse delete( UUID id) {
        userRepository.deleteById(id);
//        informationUserRepository.save(new InformationUser(user, user1, new Date(), UserStatus.ACCEPTED));
        return new ApiResponse("Successfully deleted", true);
    }



    public ApiResponse searchUser(String search, Integer roles_id, boolean enabled, Integer categoryId, Integer page, Integer size)
            throws IllegalAccessException {
        Page<User> users = null;
        System.out.println(search);

// search ishlidi
        if (roles_id == 10 && categoryId == null && !search.equals("777")) {
            users = userRepository.findAllByEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase
                    (enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));
        } else if
            //  role ishlidi
        (categoryId == null && search.equals("777") && roles_id != null) {
            users = userRepository.findAllByEnabledAndRolesId(enabled, roles_id, CommonUtills.simplePageable(page, size));

//             categorya ishlidi
        } else if (roles_id == null && search.equals("777") && categoryId != null) {
            users = userRepository.findAllByEnabledAndCategoriesIdIn(enabled, singleton(categoryId), CommonUtills.simplePageable(page, size));

            // categorya bn role ishlidi
        } else if (roles_id != null && categoryId != null && search.equals("777")) {
            users = userRepository.findAllByEnabledAndCategoriesIdInAndRolesId(enabled, singleton(categoryId), roles_id, CommonUtills.simplePageable(page, size));

            // categorya va role search ham ishlidi
        } else if (roles_id != null && categoryId != null && !search.equals("777")) {
            users = userRepository.findAllByRolesIdAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(roles_id, Collections.singleton(categoryId), enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));

            // role bn search
        }else if (categoryId==null && roles_id !=null && !search.equals("777")  ){

        users=userRepository.findAllByRolesIdAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(roles_id,  enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));

        //  category bn search
        }else if (roles_id==null && categoryId!=null && !search.equals("777")){
            users=userRepository.findAllByCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase( Collections.singleton((categoryId)), enabled, search, search, search, search, search, CommonUtills.simplePageable(page, size));
        }
        return new ApiResponse("All user ", true, users);
    }
//


    public ApiResponse search(SearchUser searchUser) throws IllegalAccessException {


        Page<User> users = userRepository.findAllById(searchUser.getId(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));


        System.out.println(" categor id  " + searchUser.getCategoryId());
        System.out.println(" role id  " + searchUser.getRoles_id());
        System.out.println(" search uchun    " + searchUser.getSearch());
        System.out.println(" enabled  " + searchUser.isEnabled());

// search ishlidi
        if (searchUser.getRoles_id() == null && searchUser.getCategoryId() == null && !searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase
                    (searchUser.isEnabled(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("search ishladi  ");
        } else if
            //  role ishlidi
        (searchUser.getCategoryId() == null && searchUser.getSearch().equals("777") && searchUser.getRoles_id() != null) {
            users = userRepository.findAllByEnabledAndRolesId(searchUser.isEnabled(), searchUser.getRoles_id(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));

            System.out.println("role ishladi  ");
            return new ApiResponse(" role  ", true, users);


//             categorya ishlidi
        } else if (searchUser.getRoles_id() == null && searchUser.getSearch().equals("777") && searchUser.getCategoryId() != null) {
            users = userRepository.findAllByEnabledAndCategoriesIdIn(searchUser.isEnabled(), singleton(searchUser.getCategoryId()), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("categor ishladi  ");
            // categorya bn role ishlidi
        } else if (searchUser.getRoles_id() != null && searchUser.getCategoryId() != null && searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByEnabledAndCategoriesIdInAndRolesId(searchUser.isEnabled(), singleton(searchUser.getCategoryId()), searchUser.getRoles_id(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("category bn role ishladi  ");

            // categorya va role search ham ishlidi
        } else if (searchUser.getRoles_id() != null && searchUser.getCategoryId() != null && !searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByRolesIdAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("category bn role va search  ishladi  ");

            // role bn search
        } else if (searchUser.getCategoryId() == null && searchUser.getRoles_id() != null && !searchUser.getSearch().equals("777")) {
            System.out.println("search bn role ishladi  ");
            users = userRepository.findAllByRolesIdAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            return new ApiResponse(" role va searech ", true, users);

            //  category bn search
        } else if (searchUser.getRoles_id() == null && searchUser.getCategoryId() != null && !searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCaseOrFatherNameContainingIgnoringCaseOrEmailContainingIgnoringCaseOrPhoneNumberContainingIgnoringCase(singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            System.out.println("category bn search ishladi  ");

        }



        return new ApiResponse("All user ", true, users);

    }





// bu yangi qo'shilgan reviewrlarni get qilib beradi
    public ApiResponse getNewReviewer() {
        List<User> users = userRepository.findAllByEnabledFalseAndActiveFalseAndRolesId(3);
        if (users == null) {
            return new ApiResponse("Yangi qo'shilgan reviewrlar yo'q");
        }
        return new ApiResponse("Yangi qo'shilgan reviewrlar ", true, users);
    }

    // yangi qo'shilgan reviewerlarni soni
    public Integer getNewReviewerCount() {
        return (userRepository.countAllByEnabledFalseAndActiveFalseAndRolesId(3));
    }

    /**
     * yangi qo'shilgan reviewerlarni activlashtirish
     */
    public ApiResponse acceptedUser(User user, ReviewerDto reviewerDto) {
        User user1 = userRepository.getById(reviewerDto.getReviewerId());
        user1.setActive(reviewerDto.isActive());
        user1.setEnabled(reviewerDto.isActive());
        userRepository.save(user1);
        informationUserRepository.save(new InformationUser(user, user1, new Date(), UserStatus.ACCEPTED));
        return new ApiResponse("Reviewer avtivation ", true);
    }

    /**
     * MA'LUM VAQT ORALIG'IDAGI RO`YXATDAN O`TGAN USERLARNI SONI
     */
    public Integer numberOfRegistredUsers(long startTime, long endTime) {
        Timestamp start=new Timestamp(startTime);
        Timestamp end=new Timestamp(endTime);
        return userRepository.countAllByCreatedAtBetween(start, end);
    }

    /**
     * DASHBOARD UCHUN
     */
    public ApiResponse dashboard(){
        try {
            ForDashboard forDashboard = new ForDashboard();
            forDashboard.setNumberOfUsers(userRepository.countAllByRolesId(4));
            forDashboard.setNumberOfReviewers(userRepository.countAllByRolesId(3));
            forDashboard.setNumberOfRedactors(userRepository.countAllByRolesId(2));
            forDashboard.setNumberOfNewArticles(articleRepository.countAllByPayTrueAndConfirmFalse());
            forDashboard.setNumberOfInReviewArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.BEGIN_CHECK));
            forDashboard.setNumberOfIsBeingEditedArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.PREPARING_FOR_PUBLICATION));
            forDashboard.setNumberOfReadyOfPublicationArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.PREPARED_FOR_PUBLICATION));
            forDashboard.setNumberOfPublishedArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.PUBLISHED));
//            forDashboard.setNumberOfPaidAndPublishedArticles(articleRepository.countAllByPublicAndPrivateTrueAndArticleStatusName(ArticleStatusName.PUBLISHED));
            return new ApiResponse("Bajarildi", true,forDashboard );
        }catch (Exception e){
            return new ApiResponse("Xatolik Yuz berdi", false);
        }
    }



public ApiResponse defaultDeadlineAddAdministrator(DeadlineAdministratorDto administratorDto){
        DeadlineAdministrator deadlineAdministrator=new DeadlineAdministrator();
     if (administratorDto.getId()!=null){
         deadlineAdministrator = deadlineAdministratorRepository.getById(administratorDto.getId());
     }
        deadlineAdministrator.setAdministratorId(administratorDto.getAdministratorId());
        deadlineAdministrator.setDeadline(administratorDto.getDeadline());
        deadlineAdministratorRepository.save(deadlineAdministrator);

        return new ApiResponse("Saved",true);
}


}

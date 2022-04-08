package com.example.article.servise;


import com.example.article.entity.*;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.RoleName;
import com.example.article.entity.enums.UserStatus;
import com.example.article.entity.enums.Watdou;
import com.example.article.entity.template.AbsEntity;
import com.example.article.payload.*;
import com.example.article.repository.*;
import com.example.article.secret.JwtProvider;
import com.example.article.utils.AppConstants;
import com.example.article.utils.CommonUtills;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
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

    @Autowired
    OtpService otpService;

    @Autowired
    SmsService smsService;
    @Autowired
    VerifyPasswordRepository verifyPasswordRepository;

    @Autowired
    LanguageRepository languageRepository;

    public String register(SignUp signUp) {
        boolean exists = userRepository.existsByPhoneNumber(signUp.getPhoneNumber());
        if (!exists) {
            User user = new User();
            user.setPhoneNumber(signUp.getPhoneNumber());
            user.setCode(generatorCode());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRoles(roleRepository.findAllByRoleNameIn(singleton(RoleName.ROLE_USER.name())));
            user.setFirebaseToken(signUp.getFirebaseToken());
            userRepository.save(user);

//            var crudDto = new CrudDto();
//            crudDto.setEmail(signUp.getEmail());
//            crudDto.setFatherName(signUp.getFatherName());
//            crudDto.setFirstName(signUp.getFirstName());
//            crudDto.setPhoneNumber(signUp.getPhoneNumber());
//
//
//            Firestore dbFirestore = FirestoreClient.getFirestore();
//            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("crud_user").document("rfgsfse").set(crudDto);
//            System.out.println(user.getId().toString());

            return (jwtProvider.generateJwtToken(user));
        }
        return ("User is already exist");
    }

    public String registerAdmin(SignUp signUp) {
        boolean exists = userRepository.existsByPhoneNumber(signUp.getPhoneNumber());
        if (!exists) {
            User user = new User();
            user.setPhoneNumber(signUp.getPhoneNumber());
//            user.setCode(generatorCode());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRoles(roleRepository.findAllByRoleNameIn(singleton(RoleName.ROLE_ADMINISTRATOR.name())));
            user.setFirebaseToken(signUp.getFirebaseToken());
            userRepository.save(user);

//            var crudDto = new CrudDto();
//            crudDto.setEmail(signUp.getEmail());
//            crudDto.setFatherName(signUp.getFatherName());
//            crudDto.setFirstName(signUp.getFirstName());
//            crudDto.setPhoneNumber(signUp.getPhoneNumber());
//
//
//            Firestore dbFirestore = FirestoreClient.getFirestore();
//            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("crud_user").document("rfgsfse").set(crudDto);
//            System.out.println(user.getId().toString());

            return (jwtProvider.generateJwtToken(user));
        }
        return ("User is already exist");
    }

    /**
     * Yangi Reviewer qo`shish
     */
    @SneakyThrows
    public ApiResponse registerReviewer(String lastName, String firstName, String fathersName, String phoneNumber, String password, String email, Set<Integer> categoryIdList,
                                        String workPlace, String workExperience, MultipartFile file, String academicDegree, List<Integer> languages, MultipartFile passport) {
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
            System.out.println(categoryIdList + "====---");
            user.setCategories(categoryRepository.findAllByDeletedTrueAndActiveTrueAndIdIn(categoryIdList));
            user.setWorkPlace(workPlace);
            user.setWorkExperience(workExperience);
            user.setScientificWork(singletonList(attachmentService.upload1(file)));
            user.setAcademicDegree(academicDegree);

            if (languages.size() != 0) {
                List<Languages> languagesList = new ArrayList<>();
                for (Integer id : languages) {
                    Languages languages1 = languageRepository.findByDeletedFalseAndId(id).get();
                    languagesList.add(languages1);
                }
                user.setLanguages(languagesList);
            }

            user.setRoles(singletonList(roleRepository.findById(3).get()));
            user.setEnabled(true);
            user.setActive(false);
            user.setPassport(attachmentService.upload1(passport));
            System.out.println("----" + password);
            System.out.println("-----" + phoneNumber);
            userRepository.save(user);

            return new ApiResponse("Muvaffaqiyatli ro`yxatdan o`tdingiz. Akkountingiz tekshirish uchun adminstratorga yuborildi. Iltimos akkountingiz aktivlashtirilishini kuting", true, jwtProvider.generateJwtToken(user));
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
        return new ApiResponse("Saqlandi", true);
    }

    /**
     * ADMIN TOMONIDAN YANGI USER QO`SHISH
     */
    public ApiResponse addEmployee(UserDto userDto) {
        try {
            boolean exists = userRepository.existsByPhoneNumber(userDto.getPhoneNumber());
            if (exists)
                return new ApiResponse("Bunday telefon raqam tizimda mavjud!!!", false);
            User user = new User();
            List<Category> categories = new ArrayList<>();
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
            if (userDto.getRoleId() == 4)
                user.setCode(generatorCode());
            if (userDto.getLanguages().size() != 0) {
                List<Languages> languagesList = new ArrayList<>();
                for (Integer id : userDto.getLanguages()) {
                    Languages languages1 = languageRepository.findByDeletedFalseAndId(id).get();
                    languagesList.add(languages1);
                }
                user.setLanguages(languagesList);
            }
            user.setRoles(singletonList(roleRepository.getById(userDto.getRoleId())));

//            CrudDto crudDto = new CrudDto();
//            crudDto.setEmail(userDto.getEmail());
//            crudDto.setFatherName(userDto.getFatherName());
//            crudDto.setFirstName(userDto.getFirstName());
//            crudDto.setPhoneNumber(userDto.getPhoneNumber());
//
//
//            Firestore dbFirestore = FirestoreClient.getFirestore();
//            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("crud_user").document("rfgsfse").set(crudDto);

            userRepository.save(user);


            return new ApiResponse("Saqlandi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi, birozdan keyin qayta urinib ko'ring", false);
        }
    }


    public String createCRUD(SignIn crud) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("crud_user").document("777").set(crud);
            return "oxshadi";
        } catch (Exception e) {

            return "xato";
        }
    }

    public String login(SignIn signIn) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getPhoneNumber(), signIn.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User principal = (User) authenticate.getPrincipal();
        return jwtProvider.generateJwtToken(principal);
    }


    public ApiResponse edit(User user, SignUp signUp) {
        try {
            boolean exist = userRepository.existsByPhoneNumberAndIdNot(user.getPhoneNumber(), user.getId());
            String phone = user.getPhoneNumber();
            if (!signUp.getPhoneNumber().equals(phone)) {
                if (!exist)
                    return new ApiResponse("Ushbu telefon raqam orqali avval ro`yxatdan o`tilgan", false);
                else
                    user.setPhoneNumber(signUp.getPhoneNumber());
            }
            if (!signUp.getPassword().equalsIgnoreCase("xyz"))
                user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setLastName(signUp.getLastName().equals("") ? user.getLastName() : signUp.getLastName());
            user.setFirstName(signUp.getFirstName().equals("") ? user.getFirstName() : signUp.getFirstName());
            user.setFatherName(signUp.getFatherName().equals("") ? user.getFatherName() : signUp.getFatherName());
            user.setEmail(signUp.getEmail().equals("") ? user.getEmail() : signUp.getEmail());
            if (signUp.getLanguages().size() != 0) {
                List<Languages> languagesList = new ArrayList<>();
                for (Integer id : signUp.getLanguages()) {
                    Languages languages1 = languageRepository.findByDeletedFalseAndId(id).get();
                    languagesList.add(languages1);
                }
                user.setLanguages(languagesList);
            }
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
            System.out.println("Password:" + signUp.getPassword());
            System.out.println("phone:" + signUp.getPhoneNumber());
            System.out.println("tt->" + "");
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception e) {
            return new ApiResponse("tamom");
        }


//            System.out.println("----->>" + signUp.getLastName());
//            System.out.println("----->>" + signUp.getFatherName());
//            System.out.println("----->>" + signUp.getWorkExperience());
//            System.out.println("----->>" + signUp.getEmail());
//            System.out.println("popop------" + user);

//        } catch (Exception e) {
//            return new ApiResponse("Xatolik yuz berdi", false);
//        }
    }

    @SneakyThrows
    public ApiResponse editUserFromAdmin(User currentUser, SignUp signUp) {
        try {
            User user = new User();
            if (signUp.getUserId() != null) {
                user = userRepository.getById(signUp.getUserId());
            }
            System.out.println("----** " + signUp.getPhoneNumber());
            boolean exists1 = userRepository.existsByPhoneNumberAndIdNot(signUp.getPhoneNumber(), signUp.getUserId());
            if (!signUp.getPhoneNumber().equals(user.getPhoneNumber())) {
                if (exists1)
                    return new ApiResponse("Ushbu telefon raqam orqali avval ro`yxatdan o`tilgan", false);
                else
                    user.setPhoneNumber(signUp.getPhoneNumber().equals("") ? user.getPhoneNumber() : signUp.getPhoneNumber());
            }
            if (signUp.getCategoryIdList().size() != 0) {
                user.setCategories(categoryRepository.findAllByDeletedTrueAndActiveTrueAndIdIn(signUp.getCategoryIdList()));
            }
            if (!signUp.getPassword().equals("")) {
                boolean exists = userRepository.existsByPasswordAndIdNot(passwordEncoder.encode(signUp.getPassword()), user.getId());
                if (!exists)
                    user.setPassword(passwordEncoder.encode(signUp.getPassword()));
                else
                    return new ApiResponse("Ushbu parol band qilingan", false);
            }
            user.setEmail(signUp.getEmail().equals("") ? user.getEmail() : signUp.getEmail());
            user.setLastName(signUp.getLastName().equals("") ? user.getLastName() : signUp.getLastName());
            user.setFirstName(signUp.getFirstName().equals("") ? user.getFirstName() : signUp.getFirstName());
            user.setFatherName(signUp.getFatherName().equals("") ? user.getFatherName() : signUp.getFatherName());

            if (signUp.getLanguages().size() != 0) {
                List<Languages> languagesList = new ArrayList<>();
                for (Integer sign : signUp.getLanguages()) {
                    Languages languages = languageRepository.findByDeletedFalseAndId(sign).get();
                    languagesList.add(languages);
                }
                user.setLanguages(languagesList);
            }
//            user.setPhoneNumber(signUp.getPhoneNumber().equals("") ? user.getPhoneNumber() : signUp.getPhoneNumber());
            user.setAcademicDegree(signUp.getAcademicDegree().equals("") ? user.getAcademicDegree() : signUp.getAcademicDegree());
            user.setWorkExperience(signUp.getWorkExperience().equals("") ? user.getWorkExperience() : signUp.getWorkExperience());
            user.setWorkPlace(signUp.getWorkPlace().equals("") ? user.getWorkPlace() : signUp.getWorkPlace());

            InformationUser informationUser = new InformationUser();
            informationUser.setAcceptedUser(currentUser);
            informationUser.setWhenAndWho(new Date());
            informationUser.setUserStatus(UserStatus.EDIT);
            informationUser.setRedactorAndReviewer(userRepository.getById(signUp.getUserId()));
            informationUserRepository.save(informationUser);
            System.out.println("password " + signUp.getPassword() + "-----" + passwordEncoder.encode(signUp.getPassword()));
            System.out.println("phone  " + signUp.getPhoneNumber());
            userRepository.save(user);
            return new ApiResponse("Muvaffaqiyatli tahrirlandi", true);
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
            return new ApiResponse("Muvaffaqiyatli o`chirildi", true);
        }
        return new ApiResponse("Xatolik yuz berdi", false);
    }


    public ApiResponse search(SearchUser searchUser) {
        System.out.println(searchUser.isEnabled() + "==");
        List<User> users = userRepository.findAllByDeleteFalse();
//        Page<User users = userRepository.findAllByDeleteFalse(CommonUtills.simplePageable(AppConstants.DEFAULT_PAGE_NUMBER1, AppConstants.DEFAULT_PAGE_SIZE1));
//
        System.out.println(" categor id  " + searchUser.getCategoryId());
        System.out.println(" role id  " + searchUser.getRoles_id());
        System.out.println(" search uchun    " + searchUser.getSearch());
        System.out.println(" enabled  " + searchUser.isEnabled());

        // search ishlidi
        if (searchUser.getRoles_id() == null && searchUser.getCategoryId() == null && !searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByActiveTrueAndEnabledAndFirstNameContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndLastNameContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndFatherNameContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndEmailContainingIgnoringCaseAndDeleteFalseOrActiveTrueAndEnabledAndPhoneNumberContainingIgnoringCase(
                    searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.isEnabled(), searchUser.getSearch());
            System.out.println("search ishladi");
        }

        //  role ishlidi
        else if (searchUser.getCategoryId() == null && searchUser.getSearch().equals("777") && searchUser.getRoles_id() != null) {
            System.out.println(searchUser.getRoles_id() + "-------" + searchUser.isEnabled());
            users = userRepository.findAllActiveTrueAndByEnabledAndRolesIdAndDeleteFalse(searchUser.isEnabled(), searchUser.getRoles_id(), false);
            System.out.println("role ishladi  ");
            return new ApiResponse(" role  ", true, users);
        }

        //categorya ishlidi
        else if (searchUser.getRoles_id() == null && searchUser.getSearch().equals("777") && searchUser.getCategoryId() != null) {
//            users = userRepository.findAllByEnabledAndDeleteFalseAndCategoriesIdIn(searchUser.isEnabled(), singleton(searchUser.getCategoryId()), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            users = userRepository.findAllByActiveTrueAndEnabledAndDeleteFalseAndCategoriesIdIn(searchUser.isEnabled(), singleton(searchUser.getCategoryId()));
            System.out.println("categor ishladi  ");
        }

        // categorya bn role ishlidi
        else if (searchUser.getRoles_id() != null && searchUser.getCategoryId() != null && searchUser.getSearch().equals("777")) {
            users = userRepository.findAllByActiveTrueAndEnabledAndDeleteFalseAndCategoriesIdInAndRolesId(searchUser.isEnabled(), singleton(searchUser.getCategoryId()), searchUser.getRoles_id());
            for (User user : users) {
                System.out.println(user.getFirstName() + " " + user.getCategories().get(0).getName());
            }

            System.out.println("category bn role ishladi  ");
        }

        // categorya va role search ham ishlidi
        else if (searchUser.getRoles_id() != null && searchUser.getCategoryId() != null && !searchUser.getSearch().equals("777")) {
//            users = userRepository.findAllByRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            users = userRepository.findAllByActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFirstNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndLastNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndFatherNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndEmailContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndCategoriesIdInAndEnabledAndPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch());
            System.out.println("category bn role va search  ishladi  ");
        }

        // role bn search
        else if (searchUser.getCategoryId() == null && searchUser.getRoles_id() != null && !searchUser.getSearch().equals("777")) {
            System.out.println("search bn role ishladi  ");
//            users = userRepository.findAllByRolesIdAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrRolesIdAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            users = userRepository.findAllByActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrActiveTrueAndRolesIdAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch(), searchUser.getRoles_id(), searchUser.isEnabled(), searchUser.getSearch());
            return new ApiResponse(" role va searech ", true, users);
        }

        //  category bn search
        else if (searchUser.getRoles_id() == null && searchUser.getCategoryId() != null && !searchUser.getSearch().equals("777")) {
//            users = userRepository.findAllByCategoriesIdInAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrCategoriesIdInAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), CommonUtills.simplePageable(searchUser.getPage(), searchUser.getSize()));
            users = userRepository.findAllByActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndFirstNameContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndLastNameContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndFatherNameContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndEmailContainingIgnoringCaseOrActiveTrueAndCategoriesIdInAndDeleteFalseAndEnabledAndPhoneNumberContainingIgnoringCase(singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch(), singleton(searchUser.getCategoryId()), searchUser.isEnabled(), searchUser.getSearch());

            for (User user : users) {
                System.out.println(user.getFirstName() + " " + user.getCategories().get(0).getName());
            }
            System.out.println("category bn search ishladi");
        } else if (searchUser.getSearch().equals("777") && searchUser.getRoles_id() == null && searchUser.getCategoryId() == null) {
            System.out.println("Enable ishladi");
            users = userRepository.findAllByEnabled(searchUser.isEnabled());
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
            user1.setEnabled(reviewerDto.isActive());
            userRepository.save(user1);
            informationUserRepository.save(new InformationUser(user, user1, new Date(), UserStatus.ACCEPTED));
            return new ApiResponse(reviewerDto.isActive() ? "Foydalanuvchi faollashtirildi" : "Foydalanuvchi bloklandi", true);
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
        forDashboard.setNumberOfNewAndPayTrue(articleRepository.countAllByPayTrue());         //hali puli to`lanmagan maqolalar soni
        forDashboard.setNumberOfNewAndPayFalse(articleRepository.countAllByPayFalse());         //hali puli to`lanmagan maqolalar soni
        forDashboard.setNumberOfRejectedArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.REJECTED));
        forDashboard.setNumberOfRecycleArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.RECYCLE));
        forDashboard.setNumberAllArticles(articleRepository.countAllByArticleStatusName(ArticleStatusName.PUBLISHED));
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
        Optional<User> optionalUser = userRepository.findByIdAndDeleteFalse(id);
        return optionalUser.map(user -> new ApiResponse("Muvaffaqiyatli bajarildi", true, user)).orElseGet(() -> new ApiResponse("Foydalanuvchi topilmadi", false));
    }


    public int generatorCode() {
        int code = (int) (Math.random() * 1000000);
        boolean exists = userRepository.existsByCode(code);
        if (exists) {
            return generatorCode();
        } else
            return code;
    }


//    public List<Notifications> getMyNotifications(User user) {
//        return userRepository.findAllBy();
//
//
//    }

    public ApiResponse getAuthorByCode(int code) {
        Optional<User> optionalUser = userRepository.findByCode(code);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new ApiResponse(user.getLastName() + " " + user.getFirstName(), true);
        }
        return new ApiResponse("Muallif topilmadi", false);
    }

    public ApiResponse createNewPassword(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumberAndDeleteFalse(phoneNumber);
        if (optionalUser.isPresent()) {
            int OTPCode = otpService.generateOTP(optionalUser.get().getPhoneNumber());
            smsService.sendSms(optionalUser.get().getPhoneNumber(), String.valueOf(OTPCode));
            VerifyPassword verifyPassword = new VerifyPassword();
            verifyPassword.setVerifyCode(OTPCode);
            verifyPassword.setPhoneNumber(phoneNumber);
            verifyPassword.setVerifyTime(System.currentTimeMillis() + 2 * 1000 * 60);
            verifyPasswordRepository.save(verifyPassword);
            userRepository.save(optionalUser.get());
            return new ApiResponse("Tasdiqlash kodi yuborildi", true, jwtProvider.generateJwtToken(optionalUser.get()));
        }
        return new ApiResponse("Bunday telefon raqam mavjud emas", false);
    }

    public ApiResponse verifyCode(User user, VerifyCode verifyCode) {

        System.out.println("userlar " + user);
        System.out.println("code " + verifyCode.getCode());
        System.out.println("password " + verifyCode.getPassword());
        Optional<VerifyPassword> optionalVerifyPassword = verifyPasswordRepository.findByVerifyCodeAndPhoneNumber(verifyCode.getCode(), user.getPhoneNumber());
        if (optionalVerifyPassword.isPresent()) {
            VerifyPassword verifyPassword = optionalVerifyPassword.get();
            if (verifyPassword.getVerifyTime() >= System.currentTimeMillis()) {
                if (verifyPassword.getVerifyCode().equals(verifyCode.getCode())) {
                    verifyPasswordRepository.deleteById(verifyPassword.getId());
                    user.setPassword(passwordEncoder.encode(verifyCode.getPassword()));
                    userRepository.save(user);
                    return new ApiResponse("Tasdiqlandi", true, jwtProvider.generateJwtToken(user));
                }
                return new ApiResponse("Tasdiqlash kodi noto'g'ri", false);
            }
            verifyPasswordRepository.deleteById(optionalVerifyPassword.get().getId());
            return new ApiResponse("Tasdiqlash muddati tugagan", false);
        }
        return new ApiResponse("Ushbu foydalanuvchiga tasdiqlash kodi yuborilmagan", false);
    }

//    public ApiResponse editPassword(User user, String password) {
//        try {
//            user.setPassword(password);
//            userRepository.save(user);
//            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
//        } catch (Exception e) {
//            return new ApiResponse("Xatolik yuz berdi", false);
//        }
//    }

    public ApiResponse addPhoto(User user, MultipartFile file) {
        try {
            Attachment attachment = attachmentService.upload1(file);
            user.setPhotos(singletonList(attachment));
            userRepository.save(user);
            return new ApiResponse("Yuklandi", true);
        } catch (IOException e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    public List<User> allNewReviewers() {
        System.out.println("kkkrwerwe");
        return userRepository.findAllByActiveFalse();
    }


    public ApiResponse activeEdite(UUID id) {

        User user = userRepository.findByEnabledTrueAndId(id);
        user.setActive(true);
        userRepository.save(user);

        return new ApiResponse("Active", true);
    }
}

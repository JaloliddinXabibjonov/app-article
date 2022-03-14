package com.example.article.servise;

import com.example.article.entity.*;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.RoleName;
import com.example.article.entity.enums.Watdou;
import com.example.article.payload.*;
import com.example.article.repository.*;

import com.google.protobuf.Api;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    DeadlineDefaultValueRepos deadlineDefaultValueRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;


    @Autowired
    AttachmentService attachmentService;
    @Autowired
    EditorArticleRepository editorArticleRepository;


    @Autowired
    InformationArticleRepository informationArticleRepository;

    @Autowired
    AuthorsRepository authorsRepository;


    @Autowired
    SmsService smsService;

    @Autowired
    PricesOfArticleRepository pricesRepository;

    @Autowired
    NotificationFromUserRepository notificationFromUserRepository;

    @Autowired
    JournalsRepository journalsRepository;

    //        public ApiResponse addArticle(int sahifaSoni, double price, int jurnaldaChopEtishSoni, int bosmaJurnalSoni, int sertifikatSoni, boolean doi, String description, String[] author, String titleArticle, Integer categoryId, boolean publicOrPrivate, UUID userId, MultipartFile file) throws IOException {
//        System.out.println("-------"+doi);
//        Article article = new Article();
//        Optional<Category> category = categoryRepository.findById(categoryId);
//        if (category.isPresent()) {
////            for (String s : author) {
////                Authors authors = new Authors();
//////                authors.setFullName(s);
////                authors.setCode(UUID.randomUUID());
////                authorsRepository.save(authors);
////                article.setAuthors(Collections.singleton(authors));
////            }
//            article.setDescription(description);
//            article.setTitleArticle(titleArticle);
//            article.setPublicPrivate(publicOrPrivate);
//            article.setCategory(category.get());
//            article.setUser(userRepository.getById(userId));
//            article.setPay(true);
//            article.setViews(0);
//            article.setFile(attachmentService.upload1(file));
//            PricesOfArticle pricesOfArticle = new PricesOfArticle();
//            pricesOfArticle.setSahifaSoni(sahifaSoni);
//            pricesOfArticle.setJurnallardaChopEtishSoni(jurnaldaChopEtishSoni);
//            pricesOfArticle.setBosmaJurnallarSoni(bosmaJurnalSoni);
//            pricesOfArticle.setSertifikatlarSoni(sertifikatSoni);
//            pricesOfArticle.setDoi(doi);
//            pricesOfArticle.setPrice(price);
//            PricesOfArticle savedPrices = pricesRepository.save(pricesOfArticle);
//            article.setPrice(savedPrices);
//            articleRepository.save(article);
//            return new ApiResponse("Saved", true);
//        }
//        return new ApiResponse("This subject not found", false);
//    }
    public ApiResponse addArticle(AddArticleDto dto, User user, MultipartFile file) throws IOException {
        System.out.println("-------" + dto.isDoi());
        Article article = new Article();
        Set<Authors> authorsList = new HashSet<>();
        Optional<Category> category = categoryRepository.findById(dto.getCategoryId());
        if (category.isPresent()) {
            if (dto.getAuthorsList().size() != 0) {

                for (Integer s : dto.getAuthorsList()) {
                    Authors authors = new Authors();
                    authors.setCode(s);
                    User user1 = userRepository.findByCode(s).get();
                    authors.setAuthorId(user1);
                    authors.setFullname(user1.getLastName() + " " + user1.getFirstName());
                    authorsRepository.save(authors);
                    authorsList.add(authors);
                }
                authorsList.add(new Authors(user, user.getLastName() + " " + user.getFirstName(), user.getCode()));
                article.setAuthors(authorsList);
            }

            System.out.println("author" + dto.getAuthorsList());

            article.setDescription(dto.getDescription());
            article.setTitleArticle(dto.getTitleArticle());
            article.setPublicPrivate(dto.isPublicPrivate());
            article.setCategory(category.get());
            article.setUser(user);
            article.setPay(true);
            article.setViews(0);
            article.setFile(attachmentService.upload1(file));

            PricesOfArticle pricesOfArticle = new PricesOfArticle();
            pricesOfArticle.setSahifaSoni(dto.getSahifaSoni());
//            pricesOfArticle.setJurnallardaChopEtishSoni(dto.getJurnaldaChopEtishSoni());
            pricesOfArticle.setBosmaJurnallarSoni(dto.getBosmaJurnalSoni());
            pricesOfArticle.setSertifikatlarSoni(dto.getSertifikatSoni());
            pricesOfArticle.setDoi(dto.isDoi());
            pricesOfArticle.setPrice(dto.getPrice());
            PricesOfArticle savedPrices = pricesRepository.save(pricesOfArticle);
            article.setPrice(savedPrices);
//            article.setJournalId(dto.getJournalsId());
            article.setJournals(Collections.singletonList(journalsRepository.findByIdAndDeletedTrue(dto.getJournalsId())));
            articleRepository.save(article);
            return new ApiResponse("Maqola muvaffaqiyatli saqlandi", true);
        }
        return new ApiResponse("Bunday fan topilmadi", false);
    }


//        public ApiResponse editArticle(ArticleDto articleDto, User user ){
//        Optional<Article> optionalArticle = articleRepository.findById(articleDto.getId());
//        if (optionalArticle.isEmpty())
//            return new ApiResponse("Article not found");
//        if (articleDto.isConfirm() && articleDto.isRejected() == false) {
////                    return new ApiResponse("o'zgartirish mumkin emas bu tasdiqlangan ", false);
////                }
//
//
//        return new ApiResponse();
//    }


    public ApiResponse getMyArticle(User user) {
        UUID id = user.getId();
        return new ApiResponse("all", true, articleRepository.findAllByCreatedBy(id));
    }

    public ApiResponse deleteArticle(UUID id) {
        articleRepository.deleteById(id);
        return new ApiResponse("delete", true);

    }

    //    public List<Article> newMyArticle(User user) {
//
//        return articleRepository.findAllByConfirmTrueAndAdmin(user.getId());
//
//    }
//


    public void sendMessage(User user, PushNotificationRequest pushNotificationRequest) {


    }


    //// bu articli redactorsAndReviewer larga biriktiradi va notifikatsiya cho'natadi
    public ApiResponse articleAddEditors(User user, NotificationForRedacktors redacktors) {

        List<User> allByIdIn = userRepository.findAllByEnabledTrueAndIdInAndDeleteFalse(redacktors.getRedactorsAndReviewer());

        Article byId = articleRepository.getById(
                redacktors.getArticle());
//        if (user.getId().equals(informationArticleRepository.findFirstByArticleIdOrderByCreatedAtDesc(byId.getId()).getChekUser().getId())){

        for (User user1 : allByIdIn) {

//            editorArticleRepository.save(new EditorsArticle(user, user1, byId, user1.getRoles()));

            sendMessage(user1, new PushNotificationRequest());

        }

        return new ApiResponse("Reviewer And Redactors", true);
//        }
//        return new ApiResponse("Bu maqolani siz tasdiqlamagansiz va buni redaktirlashga bera olmaysiz", true );

    }

    public ApiResponse removeEditor(User user, NotificationForRedacktors notification) {

        List<User> allByIdIn = userRepository.findAllByEnabledTrueAndIdInAndDeleteFalse(notification.getRedactorsAndReviewer());

        Article byId = articleRepository.getById(notification.getArticle());
//        if (user.getId().equals(informationArticleRepository.findFirstByArticleIdOrderByCreatedAtDesc(byId.getId()).getChekUser().getId())){

        for (User user1 : allByIdIn) {

            editorArticleRepository.deleteByArticleIdAndRedactorId(byId.getId(), user1.getId());

            sendMessage(user1, new PushNotificationRequest());

        }

        return new ApiResponse("Reviewer And Redactors", true);
    }


    // bu bittalab userlarni articlga briktiradi
    public ApiResponse addAndRemoveRedactor(User user, AddRedactorDto addRedactorDto) {
        long deadline;
        if (addRedactorDto.getDeadline() == 777) {
            DeadlineDefaultValue value = deadlineDefaultValueRepository.getById(1);
            deadline = value.getDeadline() * 86400000;
        } else {
            deadline = addRedactorDto.getDeadline() * 86400000;
        }
        Article article = articleRepository.getById(addRedactorDto.getArticle());
        User userId = userRepository.findAllByEnabledTrueAndIdAndDeleteFalse(addRedactorDto.getRedactorsAndReviewer());
        Optional<EditorsArticle> editorsArticle = editorArticleRepository.findByArticleIdAndRedactorId(addRedactorDto.getArticle(), addRedactorDto
                .getRedactorsAndReviewer());
        if (informationArticleRepository.existsByArticleIdAndChekUserIdAndArticleStatusName(article.getId(), user.getId(), ArticleStatusName.CONFIRM)) {
            if (!addRedactorDto.isAddAndRemove()) {
                informationArticleRepository.save(new InformationArticle(user, userId, article, new Date(), addRedactorDto
                        .isAddAndRemove() ? Watdou.ADD : Watdou.DELETE, ArticleStatusName.NULL));
                editorArticleRepository.deleteByArticleIdAndRedactorId(article.getId(), userId.getId());
                return new ApiResponse("Articledan shu user o'chirildi", true);
            }
            if (editorsArticle.isEmpty()) {
//            if (addRedactorDto.isAddAndRemove()) {
                String not = "A new article has been attached to you";

                Integer roleId = userRepository.findByUserIdAndDeleteFalse(userId.getId());
                editorArticleRepository.save(new EditorsArticle(user, userId, article, roleId, new java.sql.Date(deadline)));
                informationArticleRepository.save(new InformationArticle(user, userId, article, new Date(), addRedactorDto
                        .isAddAndRemove() ? Watdou.ADD : Watdou.DELETE, addRedactorDto
                        .isAddAndRemove() ? ArticleStatusName.ADD : ArticleStatusName.REMOVE));
                notificationFromUserRepository.save(new NotificationFromUser(article.getId(), addRedactorDto.getRedactorsAndReviewer(), false, not));

                return new ApiResponse("Maqolaga tekshiruvchi biriktirildi", true);
            }
        }
        return new ApiResponse("Bu tekshiruvchi oldin biriktirilgan  ", false);


//        }
//
//        return new ApiResponse("sizga mumkin emas ", false);
    }


    // articlni qabul qilish yo qilmaslik
    public ApiResponse reviewerAcceptTheArticle(User user, ReviewerAndRedactorResponseDto redactorResponseDto) {
        System.out.println("------>" + redactorResponseDto.getArticleStatus());
        System.out.println("article Id    " + redactorResponseDto.getArticleId());
        boolean exists = informationArticleRepository.existsByArticleIdAndRedactorIdAndArticleStatusName(redactorResponseDto
                .getArticleId(), user
                .getId(), ArticleStatusName.I_ACCEPTED);
        if (exists) {
            return new ApiResponse("Siz bu article ni avval qabul qilgansiz", false);
        }
        List<InformationArticle> information = informationArticleRepository.findAllByArticleIdAndRedactorIdAndWatdou(redactorResponseDto
                .getArticleId(), user
                .getId(), Watdou.ADD);
        if (information == null) {
            return new ApiResponse("bu article sizga biriktirilmagan ");
        }
        long deadline = information.get(information.size() - 1).getDeadline() + System.currentTimeMillis();

        String roleAdmin = "";

        for (Role role1 : information.get(information.size() - 1).getChekUser().getRoles()) {
            roleAdmin = role1.getRoleName();
        }


        Article article = articleRepository.getById(information.get(information.size() - 1).getArticle().getId());
        if (redactorResponseDto.getArticleStatus().equals(ArticleStatusName.I_ACCEPTED)) {
            String role = "";
            for (Role roles : user.getRoles()) {
                role = roles.getRoleName();
            }
            if (role.equals(RoleName.ROLE_REVIEWER.name())) {
                article.setArticleStatusName(ArticleStatusName.BEGIN_CHECK);
                articleRepository.save(article);
            } else if (role.equals(RoleName.ROLE_REDACTOR.name())) {
                article.setArticleStatusName(ArticleStatusName.PREPARING_FOR_PUBLICATION);
                articleRepository.save(article);
            }
            informationArticleRepository.save(new InformationArticle(user, article, new Date(), ArticleStatusName.I_ACCEPTED,
                    roleAdmin + ": " + information.get(information.size() - 1).getChekUser()
                            .getLastName() + " " + information.get(information.size() - 1)
                            .getChekUser()
                            .getFirstName() + " tomonidan " + user.getLastName() + " " + user
                            .getFirstName() + "ga taqrizlash vazifasi berildi"));
            return new ApiResponse("Qabul qilindi  ", true);
        } else if (redactorResponseDto.getArticleStatus().equals(ArticleStatusName.I_DID_NOT_ACCEPT)) {
            informationArticleRepository.save(new InformationArticle(user, article, new Date(), ArticleStatusName.I_DID_NOT_ACCEPT));
            String not = user.getFirstName() + " refused to investigate the article, entitled   " + article.getTitleArticle();
            UUID administratorId = null;
            for (InformationArticle informationArticle : information) {
                administratorId = informationArticle.getChekUser().getId();
            }

            notificationFromUserRepository.save(new NotificationFromUser(redactorResponseDto.getArticleId(), user.getId(), false, not, administratorId));

            return new ApiResponse("Ok mayli bu ishing yaxshi emas sani reytinging tushib ketadi", true);
        }
        return new ApiResponse("eeeeeeeeeeeee ", false);
    }


    // redactor bn reviewrlarga message  va dedline jonatish
//    public ApiResponse sendMassageDeadlineRedactorReviewer(User user, AddRedactorDto addRedactorDto) {
//        List<InformationArticle> informationArticles = informationArticleRepository.findAllByRedactorRol(addRedactorDto.getArticle(), addRedactorDto.getRole());
//        for (InformationArticle information : informationArticles) {
//            information.setDeadline((24 * 60 * 60 * 1000 * addRedactorDto.getDeadline()) + information.getCreatedAt().getTime());
//            information.setMassage(addRedactorDto.getMassage());
//            informationArticleRepository.save(information);
//        }
//        return new ApiResponse("habar yuborildi", true);
//    }

    //    // Articlaga  reviewrlar tomondan beriladigon statuslar
    @SneakyThrows
    public ApiResponse statusesGivenToTheArticleByTheEditors(UUID userId, String description, UUID articleId, String status, MultipartFile file) {
        System.out.println(" papka    --" + description);
        try {
            User user = userRepository.findById(userId).get();
//            informationArticleRepository.existsByArticleIdAndArticleStatusNameAndRedactorIdOrArticleIdAndArticleStatusNameAndRedactorIdOrArticleIdAndArticleStatusNameAndRedactorId(articleId, ArticleStatusName.CHECK_AND_ACCEPT, user, articleId, ArticleStatusName.CHECK_AND_ACCEPT, user, )
            InformationArticle informationArticle = informationArticleRepository.findByArticleIdAndRedactorIdAndArticleStatusName(articleId, userId, ArticleStatusName.I_ACCEPTED);
            System.out.println("---->" + articleId);

            Integer roleId = user.getRoles().get(0).getId();
            if (roleId == 3) {
                Article article = articleRepository.getById(informationArticle.getArticle().getId());
//            if (informationArticle.getDeadline() <= System.currentTimeMillis()) {
//                return new ApiResponse("sizga berilgan vaqt tugati ", false);
//            }
//            else {
                if (status.equalsIgnoreCase(ArticleStatusName.CHECK_AND_ACCEPT.name())) {
                    informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.CHECK_AND_ACCEPT, file == null ? null : attachmentService.upload1(file)));

                    return new ApiResponse("Siz maqola tekshiruvini yakunladingiz", true);
                } else if (status.equalsIgnoreCase(ArticleStatusName.CHECK_AND_CANCEL.name())) {
                    informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.CHECK_AND_CANCEL, file == null ? null : attachmentService.upload1(file)));
                    return new ApiResponse("Siz maqolani rad qildingiz", true);
                } else if (status
                        .equalsIgnoreCase(ArticleStatusName.CHECK_AND_RECYCLE.name())) {
                    informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.CHECK_AND_RECYCLE, file == null ? null : attachmentService.upload1(file)));
                    String not = " The article entitled the " + article.getTitleArticle() + "was rejected for publication by " + user.getFirstName();
                    UUID administratorId = informationArticle.getChekUser().getId();
                    notificationFromUserRepository.save(new NotificationFromUser(articleId, userId, false, not, administratorId));
                    return new ApiResponse("Maqola qayta ishlashaga yuborildi", true);
                }
//            }
                return new ApiResponse("ok", true);
            } else if (roleId == 2) {
                Article article = articleRepository.getById(informationArticle.getArticle().getId());
                if (informationArticle.getDeadline() <= System.currentTimeMillis()) {
                    return new ApiResponse("Sizga berilgan vaqt tugati ", false);
                } else {
                    informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.PREPARED_FOR_PUBLICATION, file == null ? null : attachmentService.upload1(file)));
                    article.setArticleStatusName(ArticleStatusName.PREPARED_FOR_PUBLICATION);
                    articleRepository.save(article);
                    return new ApiResponse("Siz maqolani tasdiqladingiz", true);
                }
            }
            return new ApiResponse("Sizning rolingiz topilmadi", false);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    //Taqsimlangan maqoloalar
    public List<Article> getDistributeds(User user) {
        List<Article> articleList = new ArrayList<>();
        List<InformationArticle> informationArticleList = informationArticleRepository.findAllByChekUserIdAndWatdou(user.getId(), Watdou.ADD);
        for (InformationArticle article : informationArticleList) {
            UUID id = article.getArticle().getId();
            List<UUID> articleList1 = informationArticleRepository.findAllByArticleIdSAndArticleStatusName(id, ArticleStatusName.I_ACCEPTED.toString());
            for (UUID informationArticle : articleList1) {
                articleList.add(articleRepository.getById(informationArticle));
            }
        }
        return articleList;
    }

    // bu  Reviewer  va Redactor qidirish uchun categorya boyicha
    public ApiResponse getReviewerAndRedactorRandom(GetUsersRoleId getUsersRoleId) {
//        Article article = articleRepository.findByConfirmTrueAndId(getUsersRoleId.getArticleId());


        if (getUsersRoleId.getArticleId() != null) {

            System.out.println("article id " + getUsersRoleId.getArticleId());
            Article article = articleRepository.getById(getUsersRoleId.getArticleId());

            System.out.println(article.getArticleStatusName());
            Integer roleId = getUsersRoleId.getRoleId();
            if (roleId == 777) {
                if (article.getArticleStatusName().equals(ArticleStatusName.START)) {
                    roleId = 3;
                } else if ((article.getArticleStatusName().equals(ArticleStatusName.BEGIN_CHECK))) {
                    roleId = 2;
                } else if (article.getArticleStatusName().toString().equals("null")) {
                    roleId = getUsersRoleId.getRoleId();
                }
            }
            Integer categoryId = article.getCategory().getId();
            System.out.println("category  " + categoryId);
            List<User> users = userRepository.findAllByEnabledTrueAndRolesIdAndCategoriesIdAndDeleteFalse(roleId, categoryId);
            List<User2> usersList = new ArrayList<>();
            for (User user : users) {
                boolean exists = editorArticleRepository.existsByArticleIdAndRedactorId(article.getId(), user.getId());
                usersList.add(new User2(user, exists));
            }
            for (User2 user2 : usersList) {
                System.out.println("userlar:   ---->" + user2 + "\n");
            }
            return new ApiResponse("Users", true, usersList);
        } else {
            return new ApiResponse("article id null", false);
        }

    }


    //     adminstratorlar uchun articlarni statusiga qarab get qilish
    public ApiResponse newMyArticle(User user, ArticleStatusInAdmins articleStatusInAdmins) {
        System.out.println(">>>>>" + articleStatusInAdmins.getStatus());
        if (user == null) {
            return new ApiResponse("Bunday foydalanuvchi mavjud emas!");
        }
        List<Article> articles = articleRepository.findAllByConfirmTrueAndAdmin(user.getId(), articleStatusInAdmins.getStatus());
        if (articles == null) {
            return new ApiResponse("Bu userning maqolalari topilmadi");
        }
        return new ApiResponse("Ok", true, articles);
    }

    //ADMINGA YANGI QO`SHILGAN MAQOLALARNI OLIB KELISH UCHUN
    public ApiResponse getNewOllArticle() {
        List<Article> articles = articleRepository.findAllByPayTrueAndConfirmFalse();
        return new ApiResponse("New articles", true, articles);
    }


    public Integer getNumberNewArticles() {
        return articleRepository.countAllByPayTrueAndConfirmFalse();
    }

    //USERGA BIRIKTIRILGAN MAQOLALARNI OLIB KELISH UCHUN
    public ApiResponse myDuties(User user) {
        List<MyTasksDto> myTasksDtoList = new ArrayList<>();
        String roleName;
        roleName = user.getRoles().get(0).getRoleName();
        Integer roleId = user.getRoles().get(0).getId();
        if (roleName.equalsIgnoreCase("ROLE_ADMINISTRATOR")) {
            List<InformationArticle> informationArticleList = informationArticleRepository.findAllByChekUserIdAndWatdou(user.getId(), Watdou.CONFIRM);
            for (InformationArticle informationArticle : informationArticleList) {
                if (informationArticle.getArticle().getArticleStatusName().equals(ArticleStatusName.START) ||
                        informationArticle.getArticle().getArticleStatusName().equals(ArticleStatusName.BEGIN_CHECK) ||
                        informationArticle.getArticle().getArticleStatusName().equals(ArticleStatusName.PREPARING_FOR_PUBLICATION)) {
                    MyTasksDto myTasksDto = new MyTasksDto();
                    myTasksDto.setArticle(informationArticle.getArticle());
                    myTasksDto.setDeadLine(new SimpleDateFormat("dd-MM-yyyy").format(new Date(informationArticle.getDeadline())));
                    myTasksDto.setSendDate(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date(informationArticle.getCreatedAt().getTime())));
                    myTasksDtoList.add(myTasksDto);
                }
            }
            if (myTasksDtoList.size() == 0)
                return new ApiResponse("Sizda vazifalar mavjud emas", true);
            return new ApiResponse("OK", true, myTasksDtoList);

        }
        if (roleName.equalsIgnoreCase("ROLE_REDACTOR")) {
            List<InformationArticle> informationArticleList = informationArticleRepository.findAllByRedactorIdAndArticleStatusName(user.getId(), ArticleStatusName.I_ACCEPTED);
            for (InformationArticle informationArticle : informationArticleList) {
                Article article = informationArticle.getArticle();
                Integer numberOfReviewers = editorArticleRepository.countAllByArticleId(informationArticle.getArticle().getId(), roleId);
                Date deadTime = new Date(informationArticle.getDeadline());
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                String deadline = dateFormat1.format(deadTime);
                Date date = new Date(informationArticle.getCreatedAt().getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String sendDate = dateFormat.format(date);
                boolean exists = informationArticleRepository.existsByArticleIdAndArticleStatusNameAndRedactorId(informationArticle.getArticle().getId(), ArticleStatusName.PREPARED_FOR_PUBLICATION, user.getId());
                if (!exists) {
                    MyTasksDto myTasksDto = new MyTasksDto();
                    myTasksDto.setArticle(article);
                    myTasksDto.setDeadLine(deadline);
                    myTasksDto.setSendDate(sendDate);
                    myTasksDto.setNumberOfReviewers(numberOfReviewers);
                    myTasksDtoList.add(myTasksDto);
                }
            }
            if (myTasksDtoList.size() == 0)
                return new ApiResponse("Sizga biriktirlgan vazifalar mavjud emas", true);
            return new ApiResponse("OK", true, myTasksDtoList);
        } else if (roleName.equalsIgnoreCase("ROLE_REVIEWER")) {
            List<InformationArticle> informationArticleList = informationArticleRepository.findAllByRedactorIdAndArticleStatusName(user.getId(), ArticleStatusName.I_ACCEPTED);
            for (InformationArticle informationArticle : informationArticleList) {
                Article article = informationArticle.getArticle();
                Integer numberOfReviewers = editorArticleRepository.countAllByArticleId(informationArticle.getArticle().getId(), roleId);
                Date deadTime = new Date(informationArticle.getDeadline());
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                String deadline = dateFormat1.format(deadTime);
                Date date = new Date(informationArticle.getCreatedAt().getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String sendDate = dateFormat.format(date);
                boolean exists = informationArticleRepository.existsByArticleIdAndRedactorIdAndArticleStatusNameOrArticleIdAndRedactorIdAndArticleStatusNameOrArticleIdAndRedactorIdAndArticleStatusName(informationArticle.getArticle().getId(), user.getId(), ArticleStatusName.CHECK_AND_ACCEPT, informationArticle.getArticle().getId(), user.getId(), ArticleStatusName.CHECK_AND_CANCEL, informationArticle.getArticle().getId(), user.getId(), ArticleStatusName.CHECK_AND_RECYCLE);
                if (!exists) {
                    MyTasksDto myTasksDto = new MyTasksDto();
                    myTasksDto.setArticle(article);
                    myTasksDto.setDeadLine(deadline);
                    myTasksDto.setSendDate(sendDate);
                    myTasksDto.setNumberOfReviewers(numberOfReviewers);
                    myTasksDtoList.add(myTasksDto);
                }
//                if (informationArticle.getArticle().getArticleStatusName().equals(ArticleStatusName.BEGIN_CHECK)) {
//                    articleList.add(informationArticle.getArticle());
//                }
            }
            if (myTasksDtoList.size() == 0)
                return new ApiResponse("", true);
            return new ApiResponse("OK", true, myTasksDtoList);
        }
        return new ApiResponse("Sizning vazifangiz topilmadi!!!", false);
    }

    //REDACTOR VA REVIEWERLARGA BIRIKTIRILGAN YANGI MAQOLALARNI OLIB KELISH UCHUN
    public List<MyTasksDto> myNewArticles(User user) {
        List<MyTasksDto> myTasksDtoList = new ArrayList<>();
//        Integer roleId = user.getRoles().get(0).getId();
        List<EditorsArticle> articleList = editorArticleRepository.findAllByRedactorId(user.getId());
        if (articleList.size() != 0) {
            for (EditorsArticle editorsArticle : articleList) {
                Article article = editorsArticle.getArticle();
                Date date = new Date(editorsArticle.getCreatedAt().getTime());
                String sendDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
                String deadLine = String.valueOf(editorsArticle.getDeadline().getTime() / (1000 * 60 * 60 * 24));
                boolean exists = informationArticleRepository.existsByArticleIdAndArticleStatusNameAndRedactorId(article.getId(), ArticleStatusName.I_ACCEPTED, user.getId());
                if (!exists) {
                    MyTasksDto myTasksDto = new MyTasksDto(article, sendDate, deadLine);
                    myTasksDtoList.add(myTasksDto);
                }
            }
            return myTasksDtoList;
        }
        return null;


    }


//    public ApiResponse myNewAttachArticle(User user) {
//        List<EditorsArticle> editorsArticles = editorArticleRepository.findAllByRedactorId(user.getId());
//        if (editorsArticles != null) {
//        List<Article> articleList=new ArrayList<>();
//
//            for (EditorsArticle editorsArticle : editorsArticles) {
//                List<InformationArticle> informationArticles = informationArticleRepository.findAllByRedactorIdAndArticleStatusName(editorsArticle.getRedactor().getId(), ArticleStatusName.NULL);
//
//                for (InformationArticle informationArticle : informationArticles) {
//
//                }
//            }
//            return new ApiResponse(" article ", true, articleList);
//
//        }else {
//            return new ApiResponse(" sizga briktirilgan articl yo'q ", false);
//        }
//
//
//    }

    public List<MyTasksDto> getMyTasks(User user) {
        List<MyTasksDto> myTasksDtoList = new ArrayList<>();
        Integer roleId = user.getRoles().get(0).getId();
        List<EditorsArticle> articleList = editorArticleRepository.findAllByAndRedactorId(user.getId());
        if (articleList.size() != 0) {
            for (EditorsArticle editorsArticle : articleList) {
                MyTasksDto myTasksDto = new MyTasksDto();
                myTasksDto.setArticle(editorsArticle.getArticle());
                myTasksDto.setNumberOfReviewers(editorArticleRepository.countAllByArticleId(editorsArticle.getArticle().getId(), roleId));
                long time = editorsArticle.getCreatedAt().getTime();
                Date date = new Date(time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String sendDate = dateFormat.format(date);
                myTasksDto.setSendDate(sendDate);
                Date deadTime = new Date(editorsArticle.getDeadline().getTime());
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                myTasksDto.setDeadLine(dateFormat1.format(deadTime));
                myTasksDtoList.add(myTasksDto);
            }
            return myTasksDtoList;
        }
        return null;
    }

    // bu articl haqida to'liq malumot beradi
    public ApiResponse informationArticle(ReviewerAndRedactorResponseDto responseDto) {
        InformationArticle informationArticle = informationArticleRepository.findByArticleId(responseDto.getArticleId());

        Article article = articleRepository.getById(responseDto.getArticleId());
        article.setActive(responseDto.isActive());
        article.setArticleStatusName(responseDto.getArticleStatus());
        articleRepository.save(article);

        return new ApiResponse("Article", true, article);
    }

    /**
     * articleni necha marta korganini sanaydi
     */
    public void viewsArticle(UUID articleId) {
        Article article = articleRepository.getById(articleId);
        article.setViews(article.getViews() + 1);
        articleRepository.save(article);
    }

    /**
     * MAQOLANI NECHA MARTA KO`RILGANINI QAYTARADI
     */
    public Integer numberOfViews(UUID articleId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (articleOptional.isPresent())
            return articleOptional.get().getViews();
        return 0;
    }

    /**
     * MAQOLANI ADMIN TOMONIDAN STATUSINI O`ZGARTIRISH
     */
    @SneakyThrows
    public ApiResponse giveStatus(User user, GiveStatusDto statusDto, MultipartFile file) {
        try {
            Optional<Article> optionalArticle = articleRepository.findById(statusDto.getArticleId());
            if (optionalArticle.isEmpty())
                return new ApiResponse("Maqola topilmadi", false);
            Article article = optionalArticle.get();
            article.setArticleStatusName(statusDto.getStatus());
            articleRepository.save(article);
            Attachment attachment = null;
            if (file != null) {
                attachment = attachmentService.upload1(file);
            }
            informationArticleRepository.save(new InformationArticle(user, article, new Date(), article.getArticleStatusName(),
                    "Maqolaga " + article.getTitleArticle() + " status berildi", attachment, statusDto.getDescription()));
            return new ApiResponse("Maqolaning statusi o`zgartirildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    @SneakyThrows
    public ApiResponse editArticle(UUID id, MultipartFile file) {
        Article article = articleRepository.getById(id);
        article.setFile(attachmentService.upload1(file));
        articleRepository.save(article);
        return new ApiResponse("Muvaffaqiyatli tahrirlandi", true);
    }

    public ArticleInfo getArticleInfoForAdmin(UUID articleId) {
        try {
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setArticle(articleRepository.findById(articleId).get());
            List<InformationArticle> informationArticleList = informationArticleRepository.findAllByArticleId(articleId);
            List<ArticleAdminInfo> articleAdminInfoList = new ArrayList<>();
            for (InformationArticle informationArticle : informationArticleList) {
                ArticleAdminInfo articleAdminInfo = new ArticleAdminInfo();
                String format = new SimpleDateFormat("dd-MMM-yyyy | HH:mm").format(informationArticle.getWhenAndWho());
                if (informationArticle.getChekUser() == null && informationArticle.getRedactor().getRoles().get(0).getRoleName().equals(RoleName.ROLE_REVIEWER.name())) {
                    articleAdminInfo.setFullName(informationArticle.getRedactor().getLastName() + " " + informationArticle.getRedactor().getFirstName());
                    articleAdminInfo.setRole(informationArticle.getRedactor().getRoles().get(0).getRoleName());
                    articleAdminInfo.setProcessDate(format);
                    articleAdminInfo.setComment(informationArticle.getDescription());
                    articleAdminInfo.setFile(informationArticle.getAttachFile());
                    articleAdminInfo.setStatus(informationArticle.getArticleStatusName().name());
                    articleAdminInfoList.add(articleAdminInfo);
                }
            }
            articleInfo.setArticleAdminInfoList(articleAdminInfoList);
            return articleInfo;
        } catch (Exception e) {
            return new ArticleInfo();
        }
    }

    public List<ArticleAdminInfo> getArticleInfoAdmin(UUID articleId){
        List<InformationArticle> informationArticleList = informationArticleRepository.findAllByArticleId(articleId);
        List<ArticleAdminInfo> articleAdminInfoList = new ArrayList<>();
        for (InformationArticle informationArticle : informationArticleList) {
            ArticleAdminInfo articleAdminInfo = new ArticleAdminInfo();
            String format = new SimpleDateFormat("dd-MMM-yyyy | HH:mm").format(informationArticle.getWhenAndWho());
            if (informationArticle.getRedactor() == null) {
                articleAdminInfo.setProcessDate(format);
                articleAdminInfo.setStatus(informationArticle.getArticleStatusName().name());
                articleAdminInfoList.add(articleAdminInfo);
            }
        }
        return articleAdminInfoList;
    }

    public Article getById(UUID articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        return optionalArticle.orElseGet(Article::new);
    }

    public ApiResponse sendSmsUserPrice(User CurrentUser, SendSmsUserPriceDto sendSmsUserPriceDto) {
        try {
            Article article = articleRepository.findByConfirmTrueAndId(sendSmsUserPriceDto.getArticleId());
            User user = userRepository.findByEnabledTrueAndId(article.getUser().getId());

//            article.setPrice(sendSmsUserPriceDto.getNewPrice());

            smsService.sendSms(user.getPhoneNumber(), sendSmsUserPriceDto.getText());
            articleRepository.save(article);


            return new ApiResponse("ok", true, user);
        } catch (Exception e) {

            return new ApiResponse("tamom ", false);
        }

    }

    public List<Article> getMyPublishedArticles(User user) {
        return articleRepository.findAllByArticleStatusNameAndUserId(ArticleStatusName.PUBLISHED, user.getId());
    }

    public List<Article> getMyCanceledArticles(User user) {
        return articleRepository.findAllByArticleStatusNameAndUserId(ArticleStatusName.RECYCLE, user.getId());
    }

    public List<Article> getMyPreparedForPublicationArticles(User user) {
        return articleRepository.findAllByArticleStatusNameAndUserId(ArticleStatusName.PREPARED_FOR_PUBLICATION, user.getId());
    }

    public List<Article> getMyCheckingArticles(User user) {
        return articleRepository.findAllByArticleStatusNameAndUserIdOrArticleStatusNameAndUserId(ArticleStatusName.BEGIN_CHECK, user.getId(), ArticleStatusName.PREPARING_FOR_PUBLICATION, user.getId());
    }

    public List<Article> getMyRejectedArticles(User user) {
        return articleRepository.findAllByArticleStatusNameAndUserId(ArticleStatusName.REJECTED, user.getId());
    }

    public List<Article> getMyCopyRightedArticles(User user) {
        return articleRepository.findAllByAuthorsCode(user.getCode());
    }


    public List<ArticleAdminInfo> getArticleInfoForRedactor(UUID articleId) {
        List<InformationArticle> informationArticleList = informationArticleRepository.findAllByArticleStatusNameAndIdOrArticleStatusNameAndIdOrArticleStatusNameAndId(ArticleStatusName.CHECK_AND_ACCEPT, articleId, ArticleStatusName.CHECK_AND_CANCEL, articleId, ArticleStatusName.CHECK_AND_RECYCLE, articleId);
        List<ArticleAdminInfo> articleAdminInfoList = new ArrayList<>();
        for (InformationArticle informationArticle : informationArticleList) {
            ArticleAdminInfo articleAdminInfo = new ArticleAdminInfo();
            articleAdminInfo.setFullName(informationArticle.getRedactor().getLastName() + " " + informationArticle.getRedactor().getFirstName());
            articleAdminInfo.setStatus(informationArticle.getArticleStatusName().name());
            articleAdminInfo.setComment(informationArticle.getDescription());
            articleAdminInfo.setFile(informationArticle.getAttachFile());
            articleAdminInfoList.add(articleAdminInfo);
        }
        return articleAdminInfoList;
    }


    public ApiResponse redactorArticle(User user, ArticleRedactorDto redactorDto) {
        Article article = articleRepository.findByConfirmTrueAndId(redactorDto.getArticleId());
        InformationArticle informationArticle = informationArticleRepository.findByArticleId(redactorDto.getArticleId());
        article.setPublishedArticle(redactorDto.getPublishedArticle());
        informationArticle.setDescription(redactorDto.getDescription());
        informationArticle.setRedactor(user);
        informationArticleRepository.save(informationArticle);
        articleRepository.save(article);
        return new ApiResponse("saved ", true);
    }


    public ApiResponse articleStatusAdministrator(User user1, GiveStatusDto statusDto) {
        Article article = articleRepository.findByConfirmTrueAndId(statusDto.getArticleId());
        User user = userRepository.findByEnabledTrueAndId(article.getUser().getId());
        String notification = statusDto.getStatus() + "--> " + statusDto.getDescription();
        if (statusDto.getStatus().toString().equals("PUBLISHED")) {

            article.setArticleStatusName(statusDto.getStatus());
            article.setJournalsActive(true);
            smsService.sendSms(user.getPhoneNumber(), notification);
            notificationFromUserRepository.save(new NotificationFromUser(user.getId(), false, notification));
            informationArticleRepository.save(new InformationArticle(user1, article, new Date(), notification));
        } else {
            article.setArticleStatusName(statusDto.getStatus());
            smsService.sendSms(user.getPhoneNumber(), notification);
            notificationFromUserRepository.save(new NotificationFromUser(user.getId(), false, notification));
            informationArticleRepository.save(new InformationArticle(user1, article, new Date(), notification));
        }
        return new ApiResponse(" Ok ", true);
    }

//    public ApiResponse getArticleByStatus(User user, String status){
//        articleRepository.findAllByArticleStatusName()
//    }


}

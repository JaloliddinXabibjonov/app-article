package com.example.article.servise;

import com.example.article.entity.*;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.RoleName;
import com.example.article.entity.enums.Watdou;
import com.example.article.payload.*;
import com.example.article.repository.*;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
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

    public ApiResponse addArticle(String description, String[] author, String titleArticle, Integer categoryId, boolean publicOrPrivate, UUID userId, MultipartFile file) throws IOException {
        Article article = new Article();
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            for (String s : author) {
                Authors authors = new Authors();
                authors.setFullName(s);
                authors.setCode(UUID.randomUUID());
                authorsRepository.save(authors);
                article.setAuthors(Collections.singleton(authors));
            }
            article.setDescription(description);
            article.setTitleArticle(titleArticle);
            article.setPublicPrivate(publicOrPrivate);
            article.setCategory(category.get());
            article.setUser(userRepository.getById(userId));
//            article.setPrice();
            article.setPay(true);
            article.setViews(0);
            article.setFile(attachmentService.upload1(file));
            articleRepository.save(article);
            return new ApiResponse("Saved", true);
        }
        return new ApiResponse("This subject not found", false);
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
        if (user.getId()
                .equals(informationArticleRepository.findFirstByArticleIdOrderByCreatedAtDesc(article.getId()).get()
                        .getChekUser()
                        .getId())) {
            if (!addRedactorDto.isAddAndRemove()) {
                informationArticleRepository.save(new InformationArticle(user, userId, article, new Date(), addRedactorDto
                        .isAddAndRemove() ? Watdou.ADD : Watdou.DELETE, ArticleStatusName.NULL));
                editorArticleRepository.deleteByArticleIdAndRedactorId(article.getId(), userId.getId());
                return new ApiResponse("Articledan shu  user o'chirildi", true);
            }
            if (editorsArticle.isEmpty()) {
//            if (addRedactorDto.isAddAndRemove()) {
                Integer roleId = userRepository.findByUserIdAndDeleteFalse(userId.getId());
                editorArticleRepository.save(new EditorsArticle(user, userId, article, roleId, new java.sql.Date(deadline)));
                informationArticleRepository.save(new InformationArticle(user, userId, article, new Date(), addRedactorDto
                        .isAddAndRemove() ? Watdou.ADD : Watdou.DELETE, addRedactorDto
                        .isAddAndRemove() ? ArticleStatusName.ADD : ArticleStatusName.REMOVE));
                return new ApiResponse("Maqolaga user biriktirildi", true);
            }
        }
        return new ApiResponse("bu user oldin biriktirilgan  ", false);


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
        InformationArticle information = informationArticleRepository.findByArticleIdAndRedactorIdAndWatdou(redactorResponseDto
                .getArticleId(), user
                .getId(), Watdou.ADD);
        if (information == null) {
            return new ApiResponse("bu article sizga biriktirilmagan ");
        }
        long deadline = information.getDeadline() + System.currentTimeMillis();

        String roleAdmin = "";

        for (Role role1 : information.getChekUser().getRoles()) {
            roleAdmin = role1.getRoleName();
        }


        Article article = articleRepository.getById(information.getArticle().getId());
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
                    roleAdmin + ": " + information.getChekUser()
                            .getLastName() + " " + information
                            .getChekUser()
                            .getFirstName() + " tomonidan " + user.getLastName() + " " + user
                            .getFirstName() + "ga taqrizlash vazifasi berildi"));
            return new ApiResponse("Qabul qilindi  ", true);
        } else if (redactorResponseDto.getArticleStatus().equals(ArticleStatusName.I_DID_NOT_ACCEPT)) {
            informationArticleRepository.save(new InformationArticle(user, article, new Date(), ArticleStatusName.I_DID_NOT_ACCEPT));
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

    //    // Articlaga redactor yo reviewrlar tomondan beriladigon statuslar
    @SneakyThrows
    public ApiResponse statusesGivenToTheArticleByTheEditors(UUID userId, String description, UUID articleId, String status, MultipartFile file) {
        System.out.println(" papka    --" + file);
        InformationArticle informationArticle = informationArticleRepository.findByArticleIdAndRedactorIdAndArticleStatusName(articleId, userId, ArticleStatusName.I_ACCEPTED);
        System.out.println("---->" + articleId);
        User user = userRepository.findById(userId).get();
        Integer roleId = user.getRoles().get(0).getId();
        if (roleId == 3) {
            Article article = articleRepository.getById(informationArticle.getArticle().getId());
//            if (informationArticle.getDeadline() <= System.currentTimeMillis()) {
//                return new ApiResponse("sizga berilgan vaqt tugati ", false);
//            }
//            else {
            if (status.equalsIgnoreCase(ArticleStatusName.CHECK_AND_ACCEPT.name())) {
                informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.CHECK_AND_ACCEPT, attachmentService.upload1(file)));

                return new ApiResponse("Siz maqola tekshiruvini yakunladingiz", true);
            } else if (status
                    .equalsIgnoreCase(ArticleStatusName.CHECK_AND_CANCEL.name())) {
                informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.CHECK_AND_CANCEL, attachmentService.upload1(file)));
                return new ApiResponse("Siz maqola tekshiruvini yakunladingiz", true);
            } else if (status
                    .equalsIgnoreCase(ArticleStatusName.CHECK_AND_RECYCLE.name())) {
                informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.CHECK_AND_RECYCLE, attachmentService.upload1(file)));
                return new ApiResponse("Maqola qayta ishlashaga yuborildi", true);
            }
//            }
            return new ApiResponse("ok", true);
        } else if (roleId == 2) {
            Article article = articleRepository.getById(informationArticle.getArticle().getId());
            if (informationArticle.getDeadline() <= System.currentTimeMillis()) {
                return new ApiResponse("Sizga berilgan vaqt tugati ", false);
            } else {
                informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.PREPARED_FOR_PUBLICATION, attachmentService.upload1(file)));
                return new ApiResponse("Siz maqolani tasdiqladingiz", true);
            }
        }
        return new ApiResponse("Sizning rolingiz topilmadi", false);
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

        Article article = articleRepository.getById(getUsersRoleId.getArticleId());
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
            System.out.println("\nsdasdsa   ---->" + user2 + "\n");
        }
        return new ApiResponse("Users", true, usersList);
    }


    // adminstratorlar uchun articlarni statusiga qarab get qilish
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
        Integer roleId = user.getRoles().get(0).getId();
        List<EditorsArticle> articleList = editorArticleRepository.findAllByAndRedactorId(user.getId());
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
                } else continue;
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
    public ApiResponse giveStatus(User user, UUID articleId, ArticleStatusName statusName) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty())
            return new ApiResponse("Maqola topilmadi", false);
        Article article = optionalArticle.get();
        article.setArticleStatusName(statusName);
        articleRepository.save(article);
        informationArticleRepository.save(new InformationArticle(user, article, new Date(), article.getArticleStatusName(),
                "Maqolaga " + article.getTitleArticle() + " status berildi"));
        return new ApiResponse("Maqolaning statusi " + statusName + " ga o`zgartirldi", true);
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

    public ArticleInfo getArticleInfo(UUID articleId) {
        try {
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setArticle(articleRepository.findById(articleId).get());
            List<InformationArticle> informationArticleList = informationArticleRepository.findAllByArticleId(articleId);
            List<ArticleAdminInfo> articleAdminInfoList = new ArrayList<>();
            for (InformationArticle informationArticle : informationArticleList) {
                ArticleAdminInfo articleAdminInfo = new ArticleAdminInfo();
                String format = new SimpleDateFormat("dd-MMM-yyyy | HH:mm").format(informationArticle.getWhenAndWho());
                if (informationArticle.getChekUser() == null) {
                    articleAdminInfo.setFullName(informationArticle.getRedactor().getLastName() + " " + informationArticle.getRedactor().getFirstName());
                    articleAdminInfo.setRole(informationArticle.getRedactor().getRoles().get(0).getRoleName());
                }
                else if (informationArticle.getRedactor() == null) {
                    articleAdminInfo.setFullName(informationArticle.getChekUser().getLastName() + " " + informationArticle.getChekUser().getFirstName());
                    articleAdminInfo.setRole(informationArticle.getChekUser().getRoles().get(0).getRoleName());
                }else {
                    articleAdminInfo.setFullName(informationArticle.getChekUser().getLastName() + " " + informationArticle.getChekUser().getFirstName());
                    articleAdminInfo.setRole(informationArticle.getChekUser().getRoles().get(0).getRoleName());
                }articleAdminInfo.setProcessDate(format);
                if (informationArticle.getArticleStatusName().equals(ArticleStatusName.ADD))
                    articleAdminInfo.setStatus(informationArticle.getArticleStatusName().name() + " (" + informationArticle.getRedactor().getLastName() + " " + informationArticle.getRedactor().getFirstName() + ")");
                else
                    articleAdminInfo.setStatus(informationArticle.getArticleStatusName().name());
                articleAdminInfo.setComment(informationArticle.getDescription());
                articleAdminInfo.setFile(informationArticle.getAttachFile());
                articleAdminInfoList.add(articleAdminInfo);
            }
            articleInfo.setArticleAdminInfoList(articleAdminInfoList);
            return articleInfo;
        } catch (Exception e) {
            return new ArticleInfo();
        }
    }

    public Article getById(UUID articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        return optionalArticle.orElseGet(Article::new);
    }

    public ApiResponse sendSmsUserPrice(User CurrentUser, SendSmsUserPriceDto sendSmsUserPriceDto) {
        try {
            Article article = articleRepository.findByConfirmTrueAndId(sendSmsUserPriceDto.getArticleId());
            User user = userRepository.findByEnabledTrueAndId(article.getUser().getId());

            article.setPrice(sendSmsUserPriceDto.getNewPrice());

            smsService.sendSms(user.getPhoneNumber(), sendSmsUserPriceDto.getText());
            articleRepository.save(article);


            return new ApiResponse("ok", true, user);
        } catch (Exception e) {

            return new ApiResponse("tamom ", false);
        }

    }
}

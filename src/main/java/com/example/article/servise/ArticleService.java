package com.example.article.servise;

import com.example.article.entity.*;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.RoleName;
import com.example.article.entity.enums.Watdou;
import com.example.article.payload.*;
import com.example.article.repository.*;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.*;
import java.lang.module.ResolutionException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Autowired
    ArticleEditArxivRepository articleEditArxivRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;
    @Autowired
    LanguageRepository languageRepository;

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
            }
            article.setAuthors(authorsList);
            System.out.println("author" + dto.getAuthorsList());
            article.setDescription(dto.getDescription());
            article.setTitleArticle(dto.getTitleArticle());
            article.setPublicPrivate(dto.isPublicPrivate());
            article.setCategory(category.get());
            article.setUser(user);
            article.setPay(true);
            article.setViews(0);
            article.setActive(true);
            article.setLanguage(languageRepository.getById(dto.getLanguageId()));
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

            ArticleEditArxiv articleEditArxiv = new ArticleEditArxiv();
            articleEditArxiv.setArticleId(article.getId());
            articleEditArxiv.setArxivFile(Collections.singletonList(attachmentService.upload1(file)));
            articleEditArxiv.setDate(new Date());
            articleEditArxivRepository.save(articleEditArxiv);

            return new ApiResponse("Maqola muvaffaqiyatli saqlandi", true);
        }
        return new ApiResponse("Bunday fan topilmadi", false);
    }


    public ApiResponse editArticle(ArticleDto dto, MultipartFile file) {
        try {
            Article article = articleRepository.findByDeletedFalseAndId(dto.getId()).get();
            if (!article.isPay()) {
                Set<Authors> authorsList = new HashSet<>();
                for (Integer s : dto.getAuthorsList()) {
                    Authors authors = new Authors();
                    authors.setCode(s);
                    User user1 = userRepository.findByCode(s).get();
                    authors.setAuthorId(user1);
                    authors.setFullname(user1.getLastName() + " " + user1.getFirstName());
                    authorsRepository.save(authors);
                    authorsList.add(authors);
                }
                article.setAuthors(authorsList);
                System.out.println("author" + dto.getAuthorsList());
                article.setDescription(dto.getDescription().equals("") ? article.getDescription() : dto.getDescription());
                article.setTitleArticle(dto.getTitleArticle().equals("") ? article.getTitleArticle() : dto.getTitleArticle());
                article.setPublicPrivate(dto.isPublicPrivate());
                article.setCategory(categoryRepository.findByDeletedTrueAndActiveTrueAndId(dto.getCategoryId()));
                article.setFile(file == null ? article.getFile() : attachmentService.upload1(file));
                PricesOfArticle pricesOfArticle = article.getPrice();
                pricesOfArticle.setSahifaSoni(dto.getSahifaSoni() == null ? article.getPrice().getSahifaSoni() : dto.getSahifaSoni());
                pricesOfArticle.setSertifikatlarSoni(dto.getSertifikatSoni() == null ? article.getPrice().getSertifikatlarSoni() : dto.getSertifikatSoni());
                pricesOfArticle.setBosmaJurnallarSoni(dto.getBosmaJurnalSoni() == null ? article.getPrice().getBosmaJurnallarSoni() : dto.getBosmaJurnalSoni());
                pricesOfArticle.setDoi(dto.isDoi());
                pricesOfArticle.setPrice(dto.getPrice() == null ? article.getPrice().getPrice() : dto.getPrice());
                PricesOfArticle savedPrices = pricesRepository.save(pricesOfArticle);
                article.setPrice(savedPrices);

                if (dto.getJournalsId().toString().equals("")) {
                    article.setJournals(Collections.singletonList(journalsRepository.findByIdAndDeletedTrue(dto.getJournalsId())));
                } else {
                    article.setJournals(article.getJournals());
                }
                articleRepository.save(article);
                ArticleEditArxiv articleEditArxiv = articleEditArxivRepository.findByArticleId(article.getId());
                articleEditArxiv.setArticleId(article.getId());
                articleEditArxiv.setArxivFile(Collections.singletonList(file == null ? null : attachmentService.upload1(file)));
                articleEditArxiv.setDate(new Date());
                articleEditArxivRepository.save(articleEditArxiv);
                return new ApiResponse("Maqola muvaffaqiyatli saqlandi", true);

            } else if (article.getArticleStatusName().name().equals("RECYCLE")) {
                ArticleEditArxiv articleEditArxiv = new ArticleEditArxiv();
                articleEditArxiv.setArticleId(article.getId());
                articleEditArxiv.setArxivFile(Collections.singletonList(attachmentService.upload1(file)));
                articleEditArxiv.setDate(new Date());
                articleEditArxivRepository.save(articleEditArxiv);
                article.setFile(attachmentService.upload1(file));
                article.setArticleStatusName(ArticleStatusName.RECYCLED);
                articleRepository.save(article);
                return new ApiResponse("Edit bo'ldi ");
            }

        } catch (Exception e) {
            return new ApiResponse(" Edit qilib bo'lmaydi ");
        }


        return new ApiResponse(" Tamom");
    }


    public ApiResponse getMyArticle(User user) {
        UUID id = user.getId();
        return new ApiResponse("all", true, articleRepository.findAllByCreatedBy(id));
    }

    public ApiResponse deleteArticle(UUID id) {
        Optional<Article> article = articleRepository.findByDeletedFalseAndId(id);
        if (article.isPresent()) {
            article.get().setDeleted(true);
            articleRepository.save(article.get());
            return new ApiResponse("O`chirildi", true);
        }
        return new ApiResponse("Maqola topilmadi", false);
    }


    // bu bittalab userlarni articlga briktiradi
    public ApiResponse addAndRemoveRedactor(User user, AddRedactorDto addRedactorDto) {
        long deadline;
        deadline = addRedactorDto.getDeadline();
        Article article = articleRepository.findByActiveTrueAndId(addRedactorDto.getArticle());
        User userId = userRepository.findAllByEnabledTrueAndIdAndDeleteFalse(addRedactorDto.getRedactorsAndReviewer());
        Optional<EditorsArticle> editorsArticle = editorArticleRepository.findByArticleIdAndRedactorId(addRedactorDto.getArticle(), addRedactorDto
                .getRedactorsAndReviewer());
        if (informationArticleRepository.existsByArticleIdAndChekUserIdAndArticleStatusName(article.getId(), user.getId(), ArticleStatusName.CONFIRM)) {
            if (!addRedactorDto.isAddAndRemove()) {
                informationArticleRepository.save(new InformationArticle(user, userId, deadline, article, new Date(), addRedactorDto
                        .isAddAndRemove() ? Watdou.ADD : Watdou.DELETE, ArticleStatusName.NULL));
                editorArticleRepository.deleteByArticleIdAndRedactorId(article.getId(), userId.getId());
                return new ApiResponse("Articledan shu user o'chirildi", true);
            }
            if (editorsArticle.isEmpty()) {
                String not = "A new article has been attached to you";
                Integer roleId = userRepository.findByUserIdAndDeleteFalse(userId.getId());
                editorArticleRepository.save(new EditorsArticle(user, userId, article, roleId));
                informationArticleRepository.save(new InformationArticle(user, userId, deadline, article, new Date(), addRedactorDto
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
        long deadline = information.get(information.size() - 1).getDeadline()*86400000 + System.currentTimeMillis();
        String roleAdmin = "";
        for (Role role1 : information.get(information.size() - 1).getChekUser().getRoles()) {
            roleAdmin = role1.getRoleName();
        }

        Article article = articleRepository.findByActiveTrueAndId(information.get(information.size() - 1).getArticle().getId());
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
            informationArticleRepository.save(new InformationArticle(user, article, deadline, new Date(), ArticleStatusName.I_ACCEPTED,
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
        return new ApiResponse("Noma'lum buyruq", false);
    }


    // Articlaga  reviewrlar tomondan beriladigon statuslar
    @SneakyThrows
    @Transactional
    public ApiResponse statusesGivenToTheArticleByTheEditors(UUID userId, String description, UUID articleId, String status, MultipartFile file) {
        System.out.println(" papka    --" + status);
        try {
            User user = userRepository.findById(userId).get();
//            informationArticleRepository.existsByArticleIdAndArticleStatusNameAndRedactorIdOrArticleIdAndArticleStatusNameAndRedactorIdOrArticleIdAndArticleStatusNameAndRedactorId(articleId, ArticleStatusName.CHECK_AND_ACCEPT, user, articleId, ArticleStatusName.CHECK_AND_ACCEPT, user, )
            InformationArticle informationArticle = informationArticleRepository.findByArticleIdAndRedactorIdAndArticleStatusName(articleId, userId, ArticleStatusName.I_ACCEPTED);
            System.out.println("---->" + articleId);

            Integer roleId = user.getRoles().get(0).getId();
            if (roleId == 3) {
                Article article = articleRepository.findByActiveTrueAndId(informationArticle.getArticle().getId());
                if (informationArticle.getDeadline() <= System.currentTimeMillis()) {
                    return new ApiResponse("Sizga berilgan vaqt tugagan", false);
                } else {
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
                }
                return new ApiResponse("ok", true);
            }
            else if (roleId == 2) {
                Article article = articleRepository.findByActiveTrueAndId(informationArticle.getArticle().getId());
                if (informationArticle.getDeadline() <= System.currentTimeMillis()) {
                    return new ApiResponse("Sizga berilgan vaqt tugagan", false);
                } else {
                    informationArticleRepository.save(new InformationArticle(user, description, article, new Date(), ArticleStatusName.PREPARED_FOR_PUBLICATION, file == null ? null : attachmentService.upload1(file)));
                    article.setArticleStatusName(ArticleStatusName.PREPARED_FOR_PUBLICATION);
                    article.setPublishedArticle(attachmentService.upload1(file));
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
                articleList.add(articleRepository.findByActiveTrueAndId(informationArticle));
            }
        }
        return articleList;
    }

    // bu  Reviewer  va Redactor qidirish uchun categorya boyicha
    public ApiResponse getReviewerAndRedactorRandom(GetUsersRoleId getUsersRoleId) {
//        Article article = articleRepository.findByConfirmTrueAndId(getUsersRoleId.getArticleId());

        if (getUsersRoleId.getArticleId() != null) {
            List<User> users = new ArrayList<>();
            System.out.println("article id " + getUsersRoleId.getArticleId());
            Article article = articleRepository.findByActiveTrueAndId(getUsersRoleId.getArticleId());
            Integer categoryId = article.getCategory().getId();
            System.out.println(article.getArticleStatusName());
            Integer roleId = getUsersRoleId.getRoleId();
            if (roleId == 777) {
                if (article.getArticleStatusName().equals(ArticleStatusName.START)) {
                    roleId = 3;
                    users = userRepository.findAllByEnabledTrueAndActiveTrueAndRolesIdAndCategoriesIdAndDeleteFalseAndLanguages(roleId, categoryId, article.getLanguage().getId());
                } else if ((article.getArticleStatusName().equals(ArticleStatusName.BEGIN_CHECK))) {
                    roleId = 2;
                    users = userRepository.findAllByEnabledTrueAndActiveTrueAndRolesIdAndDeleteFalseAndLanguageId(roleId, article.getLanguage().getId());
                } else if (article.getArticleStatusName().toString().equals("null")) {
                    roleId = getUsersRoleId.getRoleId();
                }
            }

            System.out.println("category  " + categoryId);

//            users = userRepository.findAllByEnabledTrueAndRolesIdAndCategoriesIdAndDeleteFalse(roleId, categoryId);
            List<User2> usersList = new ArrayList<>();
            for (User user : users) {
                boolean exists = editorArticleRepository.existsByArticleIdAndRedactorId(article.getId(), user.getId());
                usersList.add(new User2(user, exists));
            }
            return new ApiResponse("Users", true, usersList);
        } else {
            return new ApiResponse("article id null", false);
        }

    }


    //     adminstratorlar uchun articlarni statusiga qarab get qilish
    public ApiResponse newMyArticle(User user, ArticleStatusInAdmins articleStatusInAdmins) {
        System.out.println(" status " + articleStatusInAdmins.getStatus());
        List<Article> articles = null;
        if (articleStatusInAdmins.getStatus().equals("ACTIVEFALSE")) {
            articles = articleRepository.findAllByActiveFalseArticle(user.getId());
        }
//        else if (articleStatusInAdmins.getStatus().equals("START")) {
//            articleRepository.findAllByActiveTrueAndArticleStatusNameOrActiveTrueAndArticleStatusName(ArticleStatusName.START.name(), ArticleStatusName.RECYCLED.name());
//        }
//
        else {
            articles = articleRepository.findAllByConfirmTrueAndAdmin(user.getId(), articleStatusInAdmins.getStatus());
        }

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

                boolean exists = informationArticleRepository.existsByArticleIdAndArticleStatusNameAndRedactorId(article.getId(), ArticleStatusName.I_ACCEPTED, user.getId());
                if (!exists) {
                    long deadline = informationArticleRepository.findByArticleIdAndArticleStatusNameAndRedactorIdOrderByCreatedAtLimit1(article.getId(), ArticleStatusName.ADD.name(), user.getId());
                    MyTasksDto myTasksDto = new MyTasksDto(article, sendDate, String.valueOf(deadline));
                    myTasksDtoList.add(myTasksDto);
                }
            }
            return myTasksDtoList;
        }
        return null;

    }


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
//        InformationArticle informationArticle = informationArticleRepository.findByArticleId(responseDto.getArticleId());
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
                if (informationArticle.getChekUser() == null && (informationArticle.getRedactor().getRoles().get(0).getRoleName().equals(RoleName.ROLE_REVIEWER.name()) || informationArticle.getRedactor().getRoles().get(0).getRoleName().equals(RoleName.ROLE_REDACTOR.name()))) {
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

    public List<ArticleAdminInfo> getArticleInfoAdmin(UUID articleId) {
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
        System.out.println("Keldii" + statusDto.getStatus());
        Article article = articleRepository.findByConfirmTrueAndId(statusDto.getArticleId());
        User user = userRepository.findByEnabledTrueAndId(article.getUser().getId());
        String notification = statusDto.getStatus() + "--> " + statusDto.getDescription();
        if (statusDto.getStatus().toString().equals("PUBLISHED")) {
            article.setArticleStatusName(statusDto.getStatus());
            article.setJournalsActive(true);
            articleRepository.save(article);
            smsService.sendSms(user.getPhoneNumber(), notification);
            notificationFromUserRepository.save(new NotificationFromUser(user.getId(), false, notification));
            informationArticleRepository.save(new InformationArticle(user1, article, statusDto.getStatus(), new Date(), notification));
        } else if (statusDto.getStatus().toString().equals("NULL")) {
            smsService.sendSms(user.getPhoneNumber(), notification);
        } else {
            article.setArticleStatusName(statusDto.getStatus());
            smsService.sendSms(user.getPhoneNumber(), notification);
            articleRepository.save(article);
            notificationFromUserRepository.save(new NotificationFromUser(user.getId(), false, notification));
            informationArticleRepository.save(new InformationArticle(user1, article, new Date(), statusDto.getStatus(), notification));
        }
        articleRepository.save(article);
        return new ApiResponse(" Ok ", true);
    }

//    public ApiResponse getArticleByStatus(User user, String status){
//        articleRepository.findAllByArticleStatusName()
//    }

    public List<ArticlesForReviewers> getMyOldArticles(User user) {
        List<InformationArticle> informationArticleSet = new ArrayList<>();
        String roleName = user.getRoles().get(0).getRoleName();
        if (roleName.equals(RoleName.ROLE_REVIEWER.name())) {
            informationArticleSet = informationArticleRepository.findAllByRedactorIdAndArticleStatusNameOrRedactorIdAndArticleStatusNameOrRedactorIdAndArticleStatusName(
                    user.getId(), ArticleStatusName.CHECK_AND_ACCEPT,
                    user.getId(), ArticleStatusName.CHECK_AND_CANCEL,
                    user.getId(), ArticleStatusName.CHECK_AND_RECYCLE
            );
        } else if (roleName.equals(RoleName.ROLE_REDACTOR.name())) {
            informationArticleSet = informationArticleRepository.findAllByRedactorIdAndArticleStatusName(user.getId(), ArticleStatusName.PREPARED_FOR_PUBLICATION);
        }
        List<ArticlesForReviewers> articlesForReviewers = new ArrayList<>();
        for (InformationArticle informationArticle : informationArticleSet) {
            ArticlesForReviewers articles = new ArticlesForReviewers();
            articles.setId(informationArticle.getArticle().getId());
            articles.setFile(informationArticle.getArticle().getFile());
            Article article = articleRepository.getById(informationArticle.getArticle().getId());
            articles.setStatus(article.getArticleStatusName().name());
            articles.setTitle(article.getTitleArticle());
            articles.setPrintedJournalName(article.getJournals().get(0).getTitle());
            articlesForReviewers.add(articles);
        }
        return articlesForReviewers;

    }

    public ApiResponse reviewerCheckedArticles(UUID id) {
        List<InformationArticle> informationArticleSet = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findByIdAndDeleteFalse(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String roleName = user.getRoles().get(0).getRoleName();
            if (roleName.equals(RoleName.ROLE_REVIEWER.name())) {
                informationArticleSet = informationArticleRepository.findAllByRedactorIdAndArticleStatusNameOrRedactorIdAndArticleStatusNameOrRedactorIdAndArticleStatusName(
                        user.getId(), ArticleStatusName.CHECK_AND_ACCEPT,
                        user.getId(), ArticleStatusName.CHECK_AND_CANCEL,
                        user.getId(), ArticleStatusName.CHECK_AND_RECYCLE
                );
            } else if (roleName.equals(RoleName.ROLE_REDACTOR.name())) {
                informationArticleSet = informationArticleRepository.findAllByRedactorIdAndArticleStatusName(user.getId(), ArticleStatusName.PREPARED_FOR_PUBLICATION);
            }
            List<ArticlesForReviewers> articlesForReviewers = new ArrayList<>();
            for (InformationArticle informationArticle : informationArticleSet) {
                ArticlesForReviewers articles = new ArticlesForReviewers();
                articles.setId(informationArticle.getArticle().getId());
                articles.setFileId(informationArticle.getArticle().getFile().getId());
                articles.setOriginalName(informationArticle.getArticle().getFile().getOriginalName());
                articles.setContentType(informationArticle.getArticle().getFile().getContentType());
                Article article = articleRepository.getById(informationArticle.getArticle().getId());
                articles.setStatus(article.getArticleStatusName().name());
                articles.setDescription(informationArticle.getDescription());
                articles.setProcessDate(informationArticle.getCreatedAt());
                articles.setTitle(article.getTitleArticle());
                articles.setPrintedJournalName(article.getJournals().get(0).getTitle());
                articlesForReviewers.add(articles);
            }
            return new ApiResponse("OK", true, articlesForReviewers);
        }
        return new ApiResponse("Bunday foydalanuvchi topilmadi", false);
    }

    /**
     * USERLAR UCHUN BARCHA MUALLIFLIK MAQOLALARINI OLIB KELISH
     */
    public List<Article> allMyArticles(User user, String status) {
        if (status.equals("all")) {
            return articleRepository.findAllByAuthorsCode(user.getCode());
        } else {
            return articleRepository.findAllByAuthorsAndArticleStatusName(user.getCode(), status);
        }
    }

    // bu file ga saqlaganini oqidi

//    public HttpEntity<?> byteFileQuality(UUID id) {
//        try {
////            Article article = articleRepository.getById(id);
////            UUID id1 = article.getPublishedArticle().getId();
//            Attachment one = attachmentRepository.findById(id).orElseThrow(() -> new ResolutionException("getAttachmentID"));
//
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(one.getContentType()))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + one.getOriginalName() + "\"")
//                    .body(Files.readAllBytes(Paths.get(one.getPath())));
//        } catch (Exception e) {
//            return ResponseEntity.ok("Xatolik yuz berdi");
//        }
//    }

// bu bazaga saqlaganini oqididi

    public HttpEntity<?> byteFileQuality(UUID id) {
        Article article = articleRepository.getById(id);
        UUID attachId=article.getPublishedArticle().getId();
        Attachment byId = attachmentRepository.getById(attachId);
        AttachmentContent content = attachmentContentRepository.findByAttachment(byId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(byId.getContentType()))
                .header("Content-Disposition", "inline; fileName=\""+byId.getOriginalName()+"\"")
                .body(content.getBytes());
    }

//    public HttpEntity<?> byteFileQuality(UUID id) throws IOException {
//      Attachment attachment =attachmentRepository.getById(id);
//        File document = new File(attachmentRepository.getById(id).getPath());
//        return ResponseEntity.ok()
//                .contentType(
//                        MediaType.parseMediaType(Files.probeContentType(Path.of(document.getAbsolutePath()))))
//                .body(Files.readAllBytes(Path.of(document.getAbsolutePath())));
//    }


}

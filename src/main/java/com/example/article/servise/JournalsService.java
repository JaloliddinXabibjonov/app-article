package com.example.article.servise;

import com.example.article.entity.Article;
import com.example.article.entity.InformationArticle;
import com.example.article.entity.Journals;
import com.example.article.entity.enums.ArticleStatusName;
import com.example.article.entity.enums.JournalsStatus;
import com.example.article.payload.*;
import com.example.article.repository.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class JournalsService {
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
    ArticleRepository articleRepository;

    @Autowired
    AttachmentService attachmentService;


    @Autowired
    JournalsRepository journalsRepository;
    @Autowired
    InformationArticleRepository informationArticleRepository;
    @Autowired
    InformationArticleService informationArticleService;

    public ApiResponse addNewJournal(JournalsPayload journalsDto, MultipartFile cover, MultipartFile file) throws IOException {


        try {
//            if (deadline.getTime() <= System.currentTimeMillis())
//                return new ApiResponse("Maqola qabul qilish muddati hozirgi vaqtdan keyingi vaqt bo`lishi kerak!", true);
            Journals journals = new Journals();
            if (journalsDto.getPrintedDate() == 0) {
                journals.setPrintedDate(10);
            } else {
                journals.setPrintedDate(journalsDto.getPrintedDate());
            }
            journals.setJournalsStatus(JournalsStatus.NEW_JOURNALS.name());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tashkent"));
            String date = journalsDto.getDeadline();
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            journals.setDeadline(deadline);
            long dead = deadline.getTime() + journalsDto.getPrintedDate() * 1000 * 3600 * 24L;
            cal.setTimeInMillis(dead);
            int year = cal.get(Calendar.YEAR);

            if (journalsDto.getParentId() != null) {
                int allReleaseNumber = journalsRepository.findAllReleaseNumberByParentIdAndLastPublished(journalsDto.getParentId());
                journals.setAllReleasesNumber(allReleaseNumber + 1);
                Optional<Integer> optionalInteger = journalsRepository.findDatePublicationByParentIdAndDeletedTrueAndDatePublication(journalsDto.getParentId(), year, journalsDto.getTitle());
                if (optionalInteger.isPresent()) {
                    journals.setDatePublication(optionalInteger.get());
                    Optional<Integer> integerOptional = journalsRepository.findByReleaseNumberOfThisYear(journalsDto.getParentId(), year, journalsDto.getTitle());
                    if (integerOptional.isPresent())
                        journals.setReleaseNumberOfThisYear(integerOptional.get() + 1);
                    else
                        journals.setReleaseNumberOfThisYear(1);
                } else
                    journals.setDatePublication(year);
            } else {
                journals.setReleaseNumberOfThisYear(1);
                journals.setAllReleasesNumber(1);
                journals.setDateOfPublication(cal.getTime());
            }

            if ( journalsDto.getParentId() != null&&journalsDto.getTitle().equals("")) {
                Journals jour = journalsRepository.getByIdAndDeletedTrue(journalsDto.getParentId());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.getByIdAndDeletedTrue(jour.getCategory().getId()));
                journals.setTitle(jour.getTitle());
            } else {
                journals.setTitle(journalsDto.getTitle());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.getByDeletedTrueAndActiveTrueAndId(journalsDto.getCategoryId()));
            }
            journals.setFile(attachmentService.upload1(file));
            journals.setCover(attachmentService.upload1(cover));
            journals.setDescription(journalsDto.getDescription());
            journals.setISSN(journalsDto.getIssn());
            journals.setISBN(journalsDto.getIsbn());
            journals.setCertificateNumber(journalsDto.getCertificateNumber());
            journals.setDeleted(true);
            journalsRepository.save(journals);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception r) {
            return new ApiResponse("Xatolik yuz berdi, qaytadan urinib ko`ring", false);
        }
    }

    public ApiResponse deleteJournals(UUID journalsId) {
        Journals journals = journalsRepository.getByIdAndDeletedTrue(journalsId);
        journals.setDeleted(false);
        journalsRepository.save(journals);
        return new ApiResponse("delete", true);
    }


    public ApiResponse edit(UUID id, JournalsPayload journalsDto, MultipartFile cover, MultipartFile file) {
        try {
            Journals journals = journalsRepository.findByIdAndDeletedTrue(id);
            String date = journalsDto.getDeadline();
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            if (deadline.getTime() <= System.currentTimeMillis())
                return new ApiResponse("Maqola qabul qilish muddati hozirgi vaqtdan keyingi vaqt bo`lishi kerak!", true);
//            if (journalsDto.getParentId() != null) {
//                journals.setParentId(journalsDto.getParentId());
//            }
            journals.setCategory(String.valueOf(journalsDto.getCategoryId()).equals("") ? journals.getCategory() : categoryRepository.findByDeletedTrueAndActiveTrueAndId(journalsDto.getCategoryId()));
            journals.setTitle(journalsDto.getTitle().equals("") ? journals.getTitle() : journalsDto.getTitle());
            if (journalsDto.getStatus().equals("PUBLISHED")) {
//                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tashkent"));
//                int year = cal.get(Calendar.YEAR);
//                journals.setDatePublication(year);
                journals.setJournalsStatus(JournalsStatus.PUBLISHED.name());
//                journals.setDateOfPublication(cal.getTime());
            } else {
                journals.setDatePublication(0);
            }
//            if (journals.getParentId() == null) {
//                journals.setAllReleasesNumber(1);
//                journals.setReleaseNumberOfThisYear(1);
//            } else {
//                journals.setAllReleasesNumber(journalsRepository.findAllReleaseNumberByParentIdAndLastPublished(journals.getParentId()) + 1);
////                journals.setReleaseNumberOfThisYear(journalsRepository.findByReleaseNumberOfThisYear(journals.getParentId(), Calendar.getInstance(TimeZone.getTimeZone("Asia/Tashkent")).get(Calendar.YEAR), journals.getTitle()) );
//            }
            journals.setDeadline(deadline);
            journals.setDescription(journalsDto.getDescription());
            journals.setPrintedDate(journalsDto.getPrintedDate() == 0 ? 10 : journalsDto.getPrintedDate());
            journals.setISSN(journalsDto.getIssn());
            journals.setISBN(journalsDto.getIsbn());
            journals.setCertificateNumber(journalsDto.getCertificateNumber());
            journals.setCover(attachmentService.upload1(cover));
            journals.setFile(attachmentService.upload1(file));
            journals.setDeleted(true);
            journalsRepository.save(journals);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi, qaytadan urinib ko`ring", false);
        }

    }

    public List<Article> getArticlesFromMagazine(UUID id) {
        return articleRepository.journalArticles(id);
    }

    public List<Journals> getParentJournals() {
        return journalsRepository.findAllByDeletedTrueAndParentId(null);
    }

    public List<Journals> getPublishedParentJournals() {
        return journalsRepository.findAllByDeletedTrueAndParentIdAndJournalsStatus(null, JournalsStatus.PUBLISHED.name());
    }

    public List<ActiveJournalsDto> getActiveJournals() {
        System.out.println("journals ");
        List<ActiveJournalsDto> activeJournalsDtoList = new ArrayList<>();
        List<Journals> journalsList = journalsRepository.findAllByDeletedTrueAndJournalsStatus(JournalsStatus.NEW_JOURNALS.name());
        for (Journals journals : journalsList) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
            if (journals.getDeadline().getTime() <= System.currentTimeMillis()) {
                journals.setJournalsStatus(JournalsStatus.DEADLINE_OVER.name());
                journalsRepository.save(journals);
            }
            activeJournalsDto.setId(journals.getId());
            activeJournalsDto.setTitle(journals.getTitle());
            activeJournalsDto.setCover(journals.getCover());
            activeJournalsDto.setDeadline(journals.getDeadline());
            activeJournalsDtoList.add(activeJournalsDto);
        }
        return activeJournalsDtoList;
    }

    public ApiResponse getById(UUID id) {
        Optional<Journals> optionalJournals = journalsRepository.findByDeletedTrueAndId(id);
        if (optionalJournals.isPresent()) {
            GetJournalById getJournalById = new GetJournalById();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String deadline;
            if (optionalJournals.get().getDeadline() != null) {
                deadline = dateFormat.format(optionalJournals.get().getDeadline());
            } else {
                deadline = dateFormat.format(new Date());
            }

            getJournalById.setDeadline(deadline);
            getJournalById.setJournals(optionalJournals.get());
            List<Article> articles = articleRepository.journalArticles(id);
            getJournalById.setArticles(articles);
            return new ApiResponse("OK", true, getJournalById);
        }
        return new ApiResponse("Jurnal topilmadi", false);
    }


    public ApiResponse getJournals(UUID id) {
        Journals journals = journalsRepository.findByIdAndDeletedTrue(id);


        HashMap<String, String> capitals = new HashMap<>();
        capitals.put("title", journals.getTitle());
        capitals.put("Id", String.valueOf(journals.getId()));
        capitals.put("ISBN", journals.getISBN());
        System.out.println(capitals);

        return new ApiResponse("all", true, capitals);
    }

    public List<Journals> getAllJournals(UUID journalId) {
        return journalsRepository.findAllByDeletedTrueAndIdAndParentIdOrDeletedTrueAndIdOrDeletedTrueAndParentId(journalId, journalId, journalId, journalId);
    }

    public List<ActiveJournalsDto> getCategoryJournals(Integer categoryId) {
        List<ActiveJournalsDto> journalByIds = new ArrayList<>();
        List<Journals> journalsList = null;

        if (categoryId == 0) {
            journalsList = journalsRepository.findAllByDeletedTrueAndParentIdNull();
        } else {
            journalsList = journalsRepository.findAllByDeletedTrueAndParentIdNullAndCategoryId(categoryId);
        }


        for (Journals journals : journalsList) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String deadline = dateFormat.format(journals.getDeadline());
            activeJournalsDto.setDeadline(journals.getDateOfPublication());
            activeJournalsDto.setTitle(journals.getTitle());
            activeJournalsDto.setCover(journals.getCover());
            activeJournalsDto.setId(journals.getId());
            journalByIds.add(activeJournalsDto);
        }
        return journalByIds;
    }

    public List<ActiveJournalsDto> getPublishedCategoryJournals(Integer categoryId) {
        List<ActiveJournalsDto> journalByIds = new ArrayList<>();
        List<Journals> journalsList = null;
        System.out.println(" category id " + categoryId);
        if (categoryId == 0) {
            journalsList = journalsRepository.findAllByDeletedTrueAndParentIdNullAndJournalsStatus(JournalsStatus.PUBLISHED.name());

        } else {
            journalsList = journalsRepository.findAllByDeletedTrueAndParentIdNullAndCategoryIdAndJournalsStatus(categoryId, JournalsStatus.PUBLISHED.name());
        }


        for (Journals journals : journalsList) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String deadline = dateFormat.format(journals.getDeadline());
            activeJournalsDto.setDeadline(journals.getDateOfPublication());
            activeJournalsDto.setTitle(journals.getTitle());
            activeJournalsDto.setCover(journals.getCover());
            activeJournalsDto.setId(journals.getId());
            journalByIds.add(activeJournalsDto);
        }
        return journalByIds;
    }


    public List<ActiveJournalsDto> getCategoryJournalsForUsers(Integer categoryId) {
        List<ActiveJournalsDto> journalByIds = new ArrayList<>();
        List<Journals> journalsList;

        if (categoryId == 0) {
            journalsList = journalsRepository.findAllByDeletedTrueAndParentIdNullAndJournalsStatus(JournalsStatus.PUBLISHED.name());
        } else {
            journalsList = journalsRepository.findAllByDeletedTrueAndParentIdNullAndCategoryIdAndJournalsStatus(categoryId, JournalsStatus.PUBLISHED.name());
        }
        for (Journals journals : journalsList) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String deadline = dateFormat.format(journals.getDeadline());
            activeJournalsDto.setDeadline(journals.getDateOfPublication());
            activeJournalsDto.setTitle(journals.getTitle());
            activeJournalsDto.setCover(journals.getCover());
            activeJournalsDto.setId(journals.getId());
            journalByIds.add(activeJournalsDto);
        }
        return journalByIds;
    }


    public List<Integer> getYear(UUID id) {
        Journals optionalJournals = journalsRepository.findByIdAndDeletedTrue(id);
        List<Integer> years = new ArrayList<>();
        for (Integer integer : journalsRepository.findAllByDeletedTrueAndIdOrDeletedTrueAndParentId(optionalJournals.getParentId() != null ? optionalJournals.getParentId() : optionalJournals.getId(), optionalJournals.getParentId() != null ? optionalJournals.getParentId() : optionalJournals.getId())) {
            System.out.println("====>>>" + integer);
            if (integer != 0)
                years.add(integer);
        }
        years.sort(Collections.reverseOrder());
        return years;
    }

    public List<Integer> getPublishedYears(UUID id) {
        Journals optionalJournals = journalsRepository.findByIdAndDeletedTrue(id);
        List<Integer> years = new ArrayList<>();
        for (Integer integer : journalsRepository.findAllByDeletedTrueAndIdAndPublishedOrDeletedTrueAndParentIdAndPublished(optionalJournals.getParentId() != null ? optionalJournals.getParentId() : optionalJournals.getId(), optionalJournals.getParentId() != null ? optionalJournals.getParentId() : optionalJournals.getId())) {
            if (integer != 0)
                years.add(integer);
        }
        if (years.size() != 0) {
            years.sort(Collections.reverseOrder());
            return years;
        }

        return years;
    }


    public List<ActiveJournalsDto> getYearJournals(UUID id, Integer year) {
        UUID uuid;
        Journals journals1 = journalsRepository.findByIdAndDeletedTrue(id);
        if (journals1.getParentId() != null)
            uuid = journals1.getParentId();
        else
            uuid = journals1.getId();
        List<ActiveJournalsDto> activeJournalsDtoList = new ArrayList<>();
        List<Journals> journalsList = journalsRepository.findAllByDeletedTrueAndDatePublicationAndIdOrDeletedTrueAndDatePublicationAndParentId(year, uuid, year, uuid);
        for (Journals journals : journalsList) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
            activeJournalsDto.setTitle(journals.getTitle());
            activeJournalsDto.setCover(journals.getCover());
            activeJournalsDto.setId(journals.getId());
            activeJournalsDto.setYear(journals.getDatePublication());
            activeJournalsDto.setAllReleaseNumber(journals.getAllReleasesNumber());
            activeJournalsDto.setReleaseNumberOfThisYear(journals.getReleaseNumberOfThisYear());
            activeJournalsDtoList.add(activeJournalsDto);
        }
        return activeJournalsDtoList;
    }

    public JournalInfo getJournalInfoForUsers(UUID id) {
        System.out.println("juuuurrrrrrrnaaaaall" + id);
        JournalInfo journalInfo = new JournalInfo();
        Journals journals = journalsRepository.findByIdAndDeletedTrue(id);
        List<Article> articles = articleRepository.journalArticlesForJournals(id);
        List<ArticleInfoForJournal> articleInfoForJournalList = new ArrayList<>();
        for (Article article : articles) {
            ArticleInfoForJournal articleInfoForJournal = new ArticleInfoForJournal();
            articleInfoForJournal.setArticleId(article.getId());
            articleInfoForJournal.setTitleArticle(article.getTitleArticle());
            articleInfoForJournal.setFileId(article.getPublishedArticle().getId());
            articleInfoForJournal.setContentType(article.getPublishedArticle().getContentType());
            articleInfoForJournal.setOriginalName(article.getPublishedArticle().getOriginalName());
            articleInfoForJournal.setArticleViews(article.getViews());
            articleInfoForJournalList.add(articleInfoForJournal);
        }
        journalInfo.setArticleInfoForJournal(articleInfoForJournalList);
        journalInfo.setReleaseNumberOfThisYear(journals.getReleaseNumberOfThisYear());
        journalInfo.setPublishedDate(String.valueOf(journals.getDateOfPublication()));
        journalInfo.setJournalId(journals.getFile().getId());
        journalInfo.setOriginalName(journals.getFile().getOriginalName());
        journalInfo.setContentType(journals.getFile().getContentType());
        journalInfo.setCover(journals.getCover());
        return journalInfo;
    }


    public ApiResponse attachArticleToJournal(UUID id, boolean action) {
        try {
            Optional<Article> optionalArticle = articleRepository.findById(id);
            Article article = optionalArticle.get();
            if (action)
                article.setArticleStatusName(ArticleStatusName.PUBLISHED);
            article.setJournalsActive(action);
            articleRepository.save(article);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }


    }

    public List<ActiveJournalsDto> deadlineOver() {
        List<Journals> journals = journalsRepository.findAllByDeletedTrueAndJournalsStatus(JournalsStatus.DEADLINE_OVER.name());
        List<ActiveJournalsDto> activeJournals = new ArrayList<>();

        for (Journals journal : journals) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
            activeJournalsDto.setId(journal.getId());
            activeJournalsDto.setCover(journal.getCover());
            activeJournalsDto.setReleaseNumberOfThisYear(journal.getReleaseNumberOfThisYear());
            activeJournalsDto.setAllReleaseNumber(journal.getAllReleasesNumber());
            activeJournalsDto.setTitle(journal.getTitle());
            activeJournals.add(activeJournalsDto);

        }
        return activeJournals;
    }


    public List<ActiveJournalsDto> getPublishedJournalsByYear(UUID id, Integer year) {
        UUID uuid;
        Journals journals1 = journalsRepository.findByIdAndDeletedTrue(id);
        if (journals1.getParentId() != null)
            uuid = journals1.getParentId();
        else
            uuid = journals1.getId();
        List<ActiveJournalsDto> activeJournalsDtoList = new ArrayList<>();
        List<Journals> journalsList = journalsRepository.findAllByDeletedTrueAndDatePublicationAndIdAndJournalsStatusOrDeletedTrueAndDatePublicationAndParentIdAndJournalsStatus(year, uuid, JournalsStatus.PUBLISHED.name(), year, uuid, JournalsStatus.PUBLISHED.name());
        for (Journals journals : journalsList) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
            activeJournalsDto.setTitle(journals.getTitle());
            activeJournalsDto.setCover(journals.getCover());
            activeJournalsDto.setId(journals.getId());
            activeJournalsDto.setYear(journals.getDatePublication());
            activeJournalsDto.setAllReleaseNumber(journals.getAllReleasesNumber());
            activeJournalsDto.setReleaseNumberOfThisYear(journals.getReleaseNumberOfThisYear());
            activeJournalsDtoList.add(activeJournalsDto);
        }
        return activeJournalsDtoList;
    }

    public Set<JournalsDir> getJournalsCategories() {
        Set<JournalsDir> journalDirectionsSet = journalsRepository.findAllByJournalsStatusAndParentIdIsNull();
        System.out.println("====>" + "-----" + journalDirectionsSet);
        return journalDirectionsSet.size() != 0 ? journalDirectionsSet : new HashSet<>();
    }
}

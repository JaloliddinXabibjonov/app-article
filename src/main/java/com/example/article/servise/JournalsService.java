package com.example.article.servise;

import com.example.article.entity.Article;
import com.example.article.entity.InformationArticle;
import com.example.article.entity.Journals;
import com.example.article.entity.enums.JournalsStatus;
import com.example.article.payload.*;
import com.example.article.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
//

//            if (deadline.getTime() <= System.currentTimeMillis())
//                return new ApiResponse("Maqola qabul qilish muddati hozirgi vaqtdan keyingi vaqt bo`lishi kerak!", true);
            Journals journals = new Journals();
            if (journalsDto.getStatus().equalsIgnoreCase(String.valueOf(JournalsStatus.PUBLISHED))) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tashkent"));
                int year = cal.get(Calendar.YEAR);
                journals.setDatePublication(year);
                journals.setJournalsStatus(JournalsStatus.PUBLISHED.name());
                journals.setDateOfPublication(cal.getTime());
                System.out.println("------>>>>>" + journalsDto.getStatus());
            } else {
                journals.setJournalsStatus(JournalsStatus.NEW_JOURNALS.name());
                String date = journalsDto.getDeadline();
                Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                journals.setDeadline(deadline);
            }
            if (journalsDto.getParentId() != null && journalsDto.getTitle().equals("")) {
                Journals jour = journalsRepository.getByIdAndDeletedTrue(journalsDto.getParentId());
                journals.setTitle(jour.getTitle());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.getByIdAndDeletedTrue(jour.getCategory().getId()));
                System.out.println(jour.getCategory().getId());
                System.out.println("   id   " + journalsDto.getParentId());
            } else {
                journals.setTitle(journalsDto.getTitle());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.getByDeletedTrueAndActiveTrueAndId(journalsDto.getCategoryId()));
            }
            journals.setFile(attachmentService.upload1(file));
            journals.setCover(attachmentService.upload1(cover));


            System.out.println(" create add  " + journalsDto.getCreatedDate());
            System.out.println(" titile  " + journalsDto.getTitle());
            System.out.println(" category  " + journalsDto.getCategoryId());

            journals.setCategory(categoryRepository.findByIdAndDeletedTrue(journalsDto.getCategoryId()).get());
            journals.setJournalsStatus(JournalsStatus.valueOf(journalsDto.getStatus()).toString());
            journals.setReleaseNumberOfThisYear(journalsDto.getReleaseNumberOfThisYear());
            journals.setAllReleasesNumber(journalsDto.getAllReleasesNumber());


            journals.setDescription(journalsDto.getDescription());
            if (journalsDto.getPrintedDate() == 0) {
                journals.setPrintedDate(10);
            } else {
                journals.setPrintedDate(journalsDto.getPrintedDate());
            }


            journals.setISSN(journalsDto.getIssn());
            journals.setISBN(journalsDto.getIsbn());
            journals.setCertificateNumber(journalsDto.getCertificateNumber());

            journals.setDeleted(true);
            journalsRepository.save(journals);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception r) {
            System.out.println("xatoda    " + file);
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
        System.out.println("=========> " + journalsDto.getPrintedDate());
        try {
            Journals journals = journalsRepository.findByIdAndDeletedTrue(id);
            System.out.println(" journals  " + journals);
            String date = journalsDto.getDeadline();
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            if (deadline.getTime() <= System.currentTimeMillis())
                return new ApiResponse("Maqola qabul qilish muddati hozirgi vaqtdan keyingi vaqt bo`lishi kerak!", true);
            if (journalsDto.getParentId() != null) {
                journals.setParentId(journalsDto.getParentId());
            }
            journals.setCategory(String.valueOf(journalsDto.getCategoryId()).equals("") ? journals.getCategory() : categoryRepository.findByDeletedTrueAndActiveTrueAndId(journalsDto.getCategoryId()));
            journals.setTitle(journalsDto.getTitle().equals("") ? journals.getTitle() : journalsDto.getTitle());
            if (journalsDto.getStatus().equals("PUBLISHED")) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tashkent"));
                int year = cal.get(Calendar.YEAR);
                journals.setDatePublication(year);
                journals.setJournalsStatus(JournalsStatus.PUBLISHED.name());
                journals.setDateOfPublication(cal.getTime());
            } else {
                journals.setDatePublication(0);
            }
//            journals.setReleaseNumberOfThisYear();
            if (journals.getParentId() == null)
                journals.setAllReleasesNumber(1);
            else
                journals.setAllReleasesNumber(journalsRepository.findAllReleaseNumberByParentIdAndLastPublished(journalsDto.getParentId()) + 1);
            journals.setDeadline(deadline);
            journals.setDescription(journalsDto.getDescription());
            journals.setPrintedDate(journalsDto.getPrintedDate() == 0 ? 10 : journalsDto.getPrintedDate());
            journals.setISSN(journalsDto.getIssn());
            journals.setISBN(journalsDto.getIsbn());
            journals.setCertificateNumber(journalsDto.getCertificateNumber());
//            journals.setCover(attachmentService.upload1(cover));
//           journals.setFile(attachmentService.upload1(file));
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

    public List<ActiveJournalsDto> getActiveJournals() {
        List<ActiveJournalsDto> activeJournalsDtoList = new ArrayList<>();
        List<Journals> journalsList = journalsRepository.findAllByDeletedTrueAndJournalsStatus(JournalsStatus.NEW_JOURNALS.name());
        for (Journals journals : journalsList) {
            ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
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
            String deadline = dateFormat.format(optionalJournals.get().getDeadline());
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

        List<Journals> journals = journalsRepository.findAllByDeletedTrueAndIdAndParentIdOrDeletedTrueAndIdOrDeletedTrueAndParentId(journalId, journalId, journalId, journalId);

        return journals;

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
            activeJournalsDto.setDeadline(journals.getDeadline());
            activeJournalsDto.setTitle(journals.getTitle());
            activeJournalsDto.setCover(journals.getCover());
            activeJournalsDto.setId(journals.getId());
            journalByIds.add(activeJournalsDto);
        }
        return journalByIds;
    }


    public Set<Integer> getYear(UUID id) {
        return journalsRepository.findAllByDeletedTrueAndIdOrDeletedTrueAndParentId(id, id);
    }


    public List<ActiveJournalsDto> getYearJournals(UUID id, Integer year) {
        List<ActiveJournalsDto> activeJournalsDtoList = new ArrayList<>();
        List<Journals> journalsList = journalsRepository.findAllByDeletedTrueAndDatePublicationAndIdOrDeletedTrueAndDatePublicationAndParentId(year, id, year, id);
        ActiveJournalsDto activeJournalsDto = new ActiveJournalsDto();
        for (Journals journals : journalsList) {
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

    public JournalInfo getJournalInfo(UUID id) {
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
            articleInfoForJournalList.add(articleInfoForJournal);
        }
        journalInfo.setArticleInfoForJournal(articleInfoForJournalList);
        journalInfo.setReleaseNumberOfThisYear(journals.getReleaseNumberOfThisYear());
        journalInfo.setPublishedDate(String.valueOf(journals.getDateOfPublication()));
        journalInfo.setFile(journals.getFile());
        return journalInfo;
    }


    public ApiResponse attachArticleToJournal(UUID id, boolean action) {
        try {
            Optional<Article> optionalArticle = articleRepository.findById(id);
            Article article = optionalArticle.get();
            article.setJournalsActive(action);
            articleRepository.save(article);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        }
        catch (Exception e){
            return new ApiResponse("Xatolik yuz berdi", false);
        }


    }
}

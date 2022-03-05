package com.example.article.servise;

import com.example.article.entity.Article;
import com.example.article.entity.Attachment;
import com.example.article.entity.Category;
import com.example.article.entity.Journals;
import com.example.article.entity.enums.JournalsStatus;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.GetJournalById;
import com.example.article.payload.JournalsPayload;
import com.example.article.repository.*;
import lombok.SneakyThrows;
import org.glassfish.jersey.internal.util.JerseyPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    AttachmentService attachmentService;


    @Autowired
    JournalsRepository journalsRepository;


    public ApiResponse addNewJournal(JournalsPayload journalsDto, MultipartFile cover, MultipartFile file) throws IOException {
        String date = journalsDto.getDeadline();

        try {

            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(date);
//            if ()
            if (deadline.getTime() <= System.currentTimeMillis())
                return new ApiResponse("Maqola qabul qilish muddati hozirgi vaqtdan keyingi vaqt bo`lishi kerak!", true);
            Journals journals = new Journals();
            if (file != null) {
                journals.setFile(attachmentService.upload1(file));
            } else {
                journals.setFile(attachmentService.upload1(cover));
            }
            if (journalsDto.getParentId() != null) {
                Journals jour = journalsRepository.getByIdAndDeletedTrue(journalsDto.getParentId());
                journals.setTitle(jour.getTitle());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.getByDeletedTrueAndActiveTrueAndId(journalsDto.getCategoryId()));
                System.out.println("   id   " + journalsDto.getParentId());
            } else {
                journals.setTitle(journalsDto.getTitle());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.getByDeletedTrueAndActiveTrueAndId(journalsDto.getCategoryId()));
            }
//            journals.setCategory(categoryRepository.findByIdAndDeletedTrue(journalsDto.getCategoryId()).get());
            journals.setJournalsStatus(JournalsStatus.valueOf(journalsDto.getStatus()).toString());
            journals.setReleaseNumberOfThisYear(journalsDto.getReleaseNumberOfThisYear());
            journals.setAllReleasesNumber(journalsDto.getAllReleasesNumber());
            journals.setDeadline(deadline);
//            journals.setJournalsStatus(JournalsStatus.NEW_JOURNALS);
            journals.setDescription(journalsDto.getDescription());
            journals.setPrintedDate(journalsDto.getPrintedDate());
            journals.setISSN(journalsDto.getIssn());
            journals.setISBN(journalsDto.getIsbn());
            journals.setCertificateNumber(journalsDto.getCertificateNumber());
            journals.setCover(attachmentService.upload1(cover));
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


//    public ApiResponse edit(UUID id, JournalsPayload journalsPayload, MultipartFile cover, MultipartFile file) {
//        try{
//            journalsRepository.fi
//        }
//
//        return null;
//    }

    public List<Journals> getParentJournals() {
        return journalsRepository.findAllByDeletedTrueAndParentId(null);
    }

    public List<Journals> getActiveJournals() {
        return journalsRepository.findAllByDeletedTrueAndJournalsStatus(JournalsStatus.NEW_JOURNALS);
    }

    public List<Journals> getCategoryJournals(Integer categoryId) {

     List<Journals> journalsList=journalsRepository.findAllByDeletedTrueAndParentIdNullAndCategoryId( categoryId);
        for (Journals journals : journalsList) {
            System.out.println(journals.getTitle());
        }

     System.out.println();
        return journalsRepository.findAllByDeletedTrueAndParentIdNullAndCategoryId( categoryId);
    }


    public ApiResponse edit(UUID id, JournalsPayload journalsDto, MultipartFile cover, MultipartFile file) {
        try {
            Journals journals = journalsRepository.getByIdAndDeletedTrue(id);
            String date = journalsDto.getDeadline();
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            if (deadline.getTime() <= System.currentTimeMillis())
                return new ApiResponse("Maqola qabul qilish muddati hozirgi vaqtdan keyingi vaqt bo`lishi kerak!", true);
            journals.setParentId(journalsDto.getParentId() != null?journalsDto.getParentId():journals.getParentId());
            journals.setCategory(categoryRepository.getByDeletedTrueAndActiveTrueAndId(journalsDto.getCategoryId()));
            journals.setTitle(journalsDto.getTitle());
            journals.setFile(attachmentService.upload1(file));
            journals.setJournalsStatus(journalsDto.getStatus());
//            journals.setReleaseNumberOfThisYear();
            journals.setAllReleasesNumber(journalsRepository.findAllReleaseNumberByParentIdAndLastPublished(journalsDto.getParentId())+1);
            journals.setDeadline(deadline);
            journals.setJournalsStatus(journalsDto.getStatus());
            journals.setDescription(journalsDto.getDescription());
            journals.setPrintedDate(journalsDto.getPrintedDate());
            journals.setISSN(journalsDto.getIssn());
            journals.setISBN(journalsDto.getIsbn());
            journals.setCertificateNumber(journalsDto.getCertificateNumber());
            journals.setCover(attachmentService.upload1(cover));
            journals.setArticles(journalsDto.getArticles());
            journals.setDeleted(true);
            journalsRepository.save(journals);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi, qaytadan urinib ko`ring", false);
        }

    }

//    public List<Article> getArticlesFromMagazine(UUID id){
//
//    }
    public ApiResponse getById(UUID id){
        Optional<Journals> optionalJournals = journalsRepository.findByDeletedTrueAndId(id);
        if (optionalJournals.isPresent()){
            GetJournalById getJournalById=new GetJournalById();
            getJournalById.setJournals(optionalJournals.get());
            DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String deadline = dateFormat.format(optionalJournals.get().getDeadline());
            getJournalById.setDeadline(deadline);
            return new ApiResponse("OK", true,getJournalById);
        }
        return new ApiResponse("Jurnal topilmadi", false);
    }
}

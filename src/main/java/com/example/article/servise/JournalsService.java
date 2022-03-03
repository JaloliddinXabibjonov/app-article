package com.example.article.servise;

import com.example.article.entity.Category;
import com.example.article.entity.Journals;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.JournalsPayload;
import com.example.article.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public List<Category> getCategoryJournals() {
        return categoryRepository.parentCategory();
    }


    public ApiResponse addNewJournal(JournalsPayload journalsDto, MultipartFile photo, MultipartFile file) throws IOException {
        try {
            Optional<Journals> optionalJournals = journalsRepository.findByParentIdAndDeletedTrue(journalsDto.getParentId());
            if (optionalJournals.isEmpty())
                return new ApiResponse("Jurnal topilmadi", false);

            Journals journals = new Journals();
            if (journalsDto.getParentId() != null) {
                Journals jour = journalsRepository.getByIdAndDeletedTrue(journalsDto.getParentId());
                journals.setName(jour.getName());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.findById(journalsDto.getCategoryId()).get());
            }else {
                journals.setName(journalsDto.getName());
                journals.setParentId(journalsDto.getParentId());
                journals.setCategory(categoryRepository.findById(journalsDto.getCategoryId()).get());
            }
            journals.setFile(attachmentService.upload1(file));
            journals.setNumberOfThisYear(journalsDto.getNumberOfThisYear());
            journals.setGeneralNumber(journals.getGeneralNumber());
            journals.setReceivedDate(journalsDto.getReceivedDate());
            journals.setArticleReviewers(journalsDto.getArticleReviewers());
            journals.setPrintedDate(journalsDto.getPrintedDate());
            journals.setISSN(journalsDto.getIssn());
            journals.setISBN(journals.getISBN());
            journals.setCertificateOfJournals(journalsDto.getCertificateOfJournals());
            journals.setPhoto(attachmentService.upload1(photo));
            journals.setDeleted(true);
            journalsRepository.save(journals);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        } catch (Exception r) {
            return new ApiResponse("Xatolik yuz berdi, qaytadan urinib ko`ring", false);
        }
    }


}

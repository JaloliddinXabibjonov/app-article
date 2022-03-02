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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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


    public ApiResponse newAddJournals(JournalsPayload journalsDto) throws IOException {
        try {

            Journals journals = new Journals();
            if (journalsDto.getId() != null) {
                journals = journalsRepository.getById(journalsDto.getId());
            }


            journals.setName(journalsDto.getName());
//        journals.setReceivedDate(journalsDto.getReceivedDate());
            journals.setArticleReviewers(journalsDto.getArticleReviewers());
            journals.setMaqolaJurnaldaNechaKundaChiqishi(journalsDto.getMaqolaJurnaldaNechaKundaChiqishi());
            journals.setJurnalNechaKundaChiqishi(journalsDto.getJurnalNechaKundaChiqishi());
            journals.setISSN(journalsDto.getISSN());
            journals.setJurnalSertificat(journalsDto.getJurnalSertificat());
//        journals.setPhotoJournals(attachmentService.upload1(journalsPhoto));
            journals.setCategory(categoryRepository.findAllByIdIn(Collections.singleton(journalsDto.getCategoryId())));

            journalsRepository.save(journals);

        } catch (Exception r) {
            return new ApiResponse("nega oxshamadi", false);
        }


        return new ApiResponse("ok", true);
    }


}

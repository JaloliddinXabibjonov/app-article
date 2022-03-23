package com.example.article.servise;

import com.example.article.entity.Languages;
import com.example.article.payload.ApiResponse;
import com.example.article.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    @Autowired
    LanguageRepository languageRepository;

    public List<Languages> all() {
        return languageRepository.findAllByDeletedFalse();
    }

    public ApiResponse edit(Integer id, String name) {
        Optional<Languages> optionalLanguages = languageRepository.findByDeletedFalseAndId(id);
        if (optionalLanguages.isPresent()) {
            boolean exists = languageRepository.existsByNameAndIdNot(name, id);
            if (exists)
                return  new ApiResponse("Ushbu nomdagi til avval kiritilgan", true);
            Languages languages = optionalLanguages.get();
            languages.setName(name);
            languageRepository.save(languages);
            return new ApiResponse("Tahrirlandi", true);
        }
        return new ApiResponse("Bunday til topilmadi", false);
    }

    public ApiResponse delete(Integer id) {
        Optional<Languages> optionalLanguages = languageRepository.findByDeletedFalseAndId(id);
        if (optionalLanguages.isPresent()) {
            Languages languages = optionalLanguages.get();
            languages.setDeleted(true);
            languageRepository.save(languages);
            return new ApiResponse("O'chirildi", true);
        }
        return new ApiResponse("Bunday til topilmadi",false);


    }


    public ApiResponse add(String name) {
        boolean exists = languageRepository.existsByName(name);
        if (exists)
            return new ApiResponse("Ushbu nomdagi til avval kiritilgan", false);
        Languages languages = new Languages();
        languages.setName(name);
        languages.setActive(true);
        languageRepository.save(languages);
        return new ApiResponse("Muvaffaqiyatli saqlandi", true);
    }

    public List<Languages> allActives() {
        return languageRepository.findAllByActiveTrueAndDeletedFalse();
    }

    public ApiResponse changeActive(Integer id, boolean active) {
        Optional<Languages> optionalLanguages = languageRepository.findByDeletedFalseAndId(id);
        if (optionalLanguages.isPresent()) {
            Languages languages = optionalLanguages.get();
            languages.setActive(active);
            languageRepository.save(languages);
            return new ApiResponse(active ? "Aktivlashtirldi" : "Bloklandi", true);
        }
        return new ApiResponse("Bunday til topilmadi", false);


    }
}

package com.example.article.servise;

import com.example.article.entity.Attachment;
import com.example.article.entity.AttachmentContent;
import com.example.article.repository.AttachmentContentRepository;
import com.example.article.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
// import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileReadService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository contentRepository;

    public HttpEntity<?> byteFileQuality(UUID id) throws IOException {

//        try {
//            Attachment one = attachmentRepository.findById(id).orElseThrow(() -> new ResolutionException("getAttachmentID"));
//        AttachmentContent attachmentContent=contentRepository.findByAttachment(one);
//
//            System.out.println(one);
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(one.getContentType()))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + one.getOriginalName() + "\"")
//                    .body(Files.readAllBytes(Paths.get(one.getContentType())));


//        } catch (Exception e) {
//
            return ResponseEntity.ok().body("oxshamadi");
//
//
//        }


    }
}

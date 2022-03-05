package com.example.article.servise;

import com.example.article.entity.Attachment;
import com.example.article.entity.AttachmentContent;
import com.example.article.payload.ApiResponse;
import com.example.article.repository.AttachmentContentRepository;
import com.example.article.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.*;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    public Attachment upload(MultipartHttpServletRequest request) throws IOException {
        List<UUID> photoIds = new ArrayList<>();
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()){
        MultipartFile file = request.getFile(fileNames.next());
        Attachment attechment = new Attachment();
        attechment.setOriginalName(file.getOriginalFilename());
        attechment.setSize(file.getSize());
        attechment.setContentType(file.getContentType());
        attechment.setFileName(file.getName());
        attechment = attachmentRepository.save(attechment);
        AttachmentContent content = new AttachmentContent();
        content.setAttachment(attechment);
        content.setBytes(file.getBytes());
        attachmentContentRepository.save(content);
        photoIds.add(attechment.getId());
     return  attechment;
        }
  return null;
   }

    public HttpEntity<?> download(UUID id) {
        Attachment byId = attachmentRepository.getById(id);
        AttachmentContent content = attachmentContentRepository.findByAttachment(byId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(byId.getContentType()))
                .header("Content-Disposition", "attachment; fileName=\""+byId.getOriginalName()+"\"")
                .body(content.getBytes());
    }

    public Attachment upload1(MultipartFile file) throws IOException {
        Attachment attechment = new Attachment();
        attechment.setOriginalName(file.getOriginalFilename());
        attechment.setSize(file.getSize());
        attechment.setContentType(file.getContentType());
        attechment.setFileName(file.getName());
        attechment = attachmentRepository.save(attechment);
        AttachmentContent content = new AttachmentContent();
        content.setAttachment(attechment);
        content.setBytes(file.getBytes());
        attachmentContentRepository.save(content);
        return attechment;
    }




}

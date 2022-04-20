package com.example.article.servise;

import com.example.article.entity.Attachment;
import com.example.article.entity.AttachmentContent;
import com.example.article.payload.ApiResponse;
import com.example.article.repository.AttachmentContentRepository;
import com.example.article.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final String uploadDirectory="Articles";
    public Attachment uploadToSystem(MultipartFile file) throws IOException {
        if (file!=null){
            String[] split = file.getOriginalFilename().split("\\.");
            String name= UUID.randomUUID()+"."+split[split.length-1];
            Path path= Paths.get(uploadDirectory+"/"+name);
            String paths=uploadDirectory+"/"+name;
            Attachment attachment=new Attachment(file.getOriginalFilename(), file.getSize(), file.getContentType(),name,paths);
            attachmentRepository.save(attachment);
            Files.copy(file.getInputStream(), path);
            return attachment;
        }
        return null;
    }
//    public HttpEntity<?> byteFileQuality(UUID id){
//        try {
//            Attachment one = attachmentRepository.findById(id)
//                    .orElseThrow(() -> new ResolutionException("getAttachmentID"));
//                    return ResponseEntity.ok()
//                            .contentType(MediaType.parseMediaType(one.getContentType()))
//                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + one.getOriginalName() + "\"")
//                            .body(Files.readAllBytes(Paths.get(one.getPath())));
//        } catch (Exception e) {
//            return ResponseEntity.ok("Xatolik yuz berdi");
//        }
//    }


}

package com.example.article.controller;

import com.example.article.entity.Attachment;
import com.example.article.entity.AttachmentContent;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.MultipartForm;
import com.example.article.repository.AttachmentContentRepository;
import com.example.article.repository.AttachmentRepository;
import com.example.article.servise.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;
    @PostMapping(value = "/upload")
    public HttpEntity<?> upload(MultipartHttpServletRequest request) throws IOException {
        return ResponseEntity.ok(attachmentService.upload(request));
    }

    @GetMapping("/download/{id}")
    public HttpEntity<?> download(@PathVariable UUID id){
        return attachmentService.download(id);
//         ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping("/upload1")
    public HttpEntity<?> upload(@RequestPart MultipartFile request) throws IOException {
        return ResponseEntity.ok(attachmentService.upload1(request));
    }

    @PostMapping("/upload12")
    public List<Attachment> upload12(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<Attachment> attachmentList=new ArrayList<>();
        List.of(files).forEach(file -> {
            try {
                Attachment attechment = new Attachment();
                attechment.setOriginalName(file.getOriginalFilename());
                attechment.setSize(file.getSize());
                attechment.setContentType(file.getContentType());
                attechment.setFileName(file.getName());
                Attachment save = attachmentRepository.save(attechment);
                attachmentList.add(save);
                AttachmentContent content = new AttachmentContent();
                content.setAttachment(save);
                content.setBytes(file.getBytes());
                attachmentContentRepository.save(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        for (MultipartFile file : files) {
//            Attachment attechment = new Attachment();
//            attechment.setOriginalName(file.getOriginalFilename());
//            attechment.setSize(file.getSize());
//            attechment.setContentType(file.getContentType());
//            attechment.setFileName(file.getName());
//            Attachment save = attachmentRepository.save(attechment);
//            attachmentList.add(save);
//            AttachmentContent content = new AttachmentContent();
//            content.setAttachment(save);
//            content.setBytes(file.getBytes());
//            attachmentContentRepository.save(content);
//            System.out.println(attechment);
//        }

        return attachmentList;
    }
}

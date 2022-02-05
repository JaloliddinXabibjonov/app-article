package com.example.article.controller;

import com.example.article.entity.Attachment;
import com.example.article.payload.ApiResponse;
import com.example.article.servise.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @PostMapping("/upload")
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

}

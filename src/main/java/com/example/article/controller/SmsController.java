package com.example.article.controller;

import com.example.article.payload.Response;
import com.example.article.payload.SmsDto;
import com.example.article.servise.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/sms")

public class SmsController {
    @Autowired
    SmsService smsService;


    @PostMapping("/sendSms")
    public HttpEntity<?> sendSms(@RequestParam String text,@RequestParam String  recipient) throws IOException {

        smsService.sendSms(recipient,text);
        return new ResponseEntity<String>("Otp send Successfully", HttpStatus.OK);
    }

}

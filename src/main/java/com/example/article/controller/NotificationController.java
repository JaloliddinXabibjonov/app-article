package com.example.article.controller;

import com.example.article.entity.User;
import com.example.article.payload.ApiResponse;
import com.example.article.secret.CurrentUser;
import com.example.article.servise.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @PostMapping("notificationNumber")
    public Integer notificationNumber(@CurrentUser User user){
        return    notificationService.notificationNumber(user);
    }


    @PostMapping("notificationList")
    public ApiResponse notificationList(@CurrentUser User user){
        return    notificationService.notificationList(user);
    }


    @DeleteMapping("deleteNotification/{id}")
    public ApiResponse deleteNotification( @PathVariable Integer id){
        return    notificationService.deleteNotification(id);
    }



}

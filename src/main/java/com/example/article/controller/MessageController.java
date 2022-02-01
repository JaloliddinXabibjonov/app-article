package com.example.article.controller;

import com.example.article.entity.UserStorage;
import com.example.article.payload.MassageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MessageController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, MassageModel massage) {
        System.out.println("handlin massage  " + massage + " to: " + to);
        boolean isExist = UserStorage.getInstance().getUsers().contains(to);
        if (isExist) {

            simpMessagingTemplate.convertAndSend("/topic/messages" + to, massage);
        }

    }

}

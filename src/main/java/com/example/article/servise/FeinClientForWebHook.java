package com.example.article.servise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@FeignClient(name = "testFein",url = "https://api.telegram.org/bot1896120943:AAHWBHdBa7Lt-RAugfFFHkNwpUbKhbnqJ0Q")
public interface FeinClientForWebHook {

    @PostMapping("/sendMessage")
    Object sendMes(@RequestBody SendMessage sendMessage);

    @PostMapping("/sendPhoto")
    Object sendNews(@RequestBody SendPhoto sendPhoto);

}

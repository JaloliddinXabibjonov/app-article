package com.example.article.servise;

import com.example.article.entity.Article;
import com.example.article.payload.ApiResponse;
import com.example.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

@Service
public class TelegramWebHookService {
 @Autowired
    ArticleRepository articleRepository;



    private final String TOKEN = "5149377773:AAEABkZOlmYvyK5DuDXPx17H_1ex6eu45R0";
    private final String URL = "https://api.telegram.org/bot" + TOKEN;



    public long getChatId(Update update) {
        return update.getCallbackQuery() != null ?
                update.getCallbackQuery().getMessage().getChatId()
                :
                update.getMessage().getChatId();
    }









//    public ApiResponse sendNews(NewsDto newsDto)throws Exception {
//        try {
//
//            List<TelegramUser> all = telegramUserRepository.findAll();
//            for (TelegramUser user : all) {
//                SendPhoto sendPhoto = new SendPhoto();
//                sendPhoto.setChatId(user.getChatId().toString());
//                sendPhoto.setCaption(newsDto.getTitle()+"\n"+
//                                             newsDto.getDescription());
//                InputFile inputFile = new InputFile();
//                inputFile.setMedia(new File(newsDto.getPhotoUrl()), "dsf");
//                sendPhoto.setPhoto(inputFile);
//                restTemplate.postForEntity(URL+"/sendPhoto",sendPhoto,Void.class);
//            }
//            return new ApiResponse("Ok",true);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ApiResponse("Error",false);
//    }


//    public ApiResponse sendNews(){
//
//        List<Article>  articles=articleRepository.findAll();
//SendPhoto sendPhoto=new SendPhoto();
//
//sendPhoto.setCaption();
//sendPhoto.setPhoto();
//    }
//




//
//    String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
//
//    String apiToken = "my_bot_api_token";
//    String chatId = "@my_channel_name";
//    String text = "Hello world!";
//
//    urlString = String.format(urlString, apiToken, chatId, text);
//
//    URL url = new URL(urlString);
//    URLConnection conn = url.openConnection();
//
//    StringBuilder sb = new StringBuilder();
//    InputStream is = new BufferedInputStream(conn.getInputStream());
//    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//    String inputLine = "";
//while ((inputLine = br.readLine()) != null) {
//        sb.append(inputLine);
//    }
//    String response = sb.toString();
//// Do what you want with response
//

}

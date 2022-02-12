package com.example.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -12);
        for (int i = 1; i <=12; i++){
            c.add(Calendar.MONTH, +1);
            System.out.println(c.get(Calendar.MONTH)+1);
        }
        Date date=new Date();
        System.out.println("push notification"+date.getTime());
    }

}

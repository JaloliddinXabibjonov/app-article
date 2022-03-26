package com.example.article;

import com.example.article.entity.Attachment;
import com.example.article.repository.AttachmentRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.*;

@SpringBootApplication
public class ArticleApplication {


    @SneakyThrows
    public static void main(String[] args) {

        SpringApplication.run(ArticleApplication.class, args);

//task1();

    }

//    @SneakyThrows
//public static void  task1(){
//       try (  FileInputStream in=new FileInputStream("E:/1234.jpg");
//              FileOutputStream out=new FileOutputStream("E:/12345.jpg");
//       ){
//        int c;
//        while ((c=in.read())!=-1){
//
//        }
//
//       }catch (FileNotFoundException e){
//           e.printStackTrace();
//       }catch (IOException e ){
//           e.printStackTrace();
//       }





//}
}

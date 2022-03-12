package com.example.article;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.time.ZoneId;
import java.util.*;

@SpringBootApplication
public class ArticleApplication {
    @SneakyThrows
    public static void main(String[] args) {

        SpringApplication.run(ArticleApplication.class, args);
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.MONTH, -12);
//        for (int i = 1; i <=12; i++){
//            c.add(Calendar.MONTH, +1);
//            System.out.println(c.get(Calendar.MONTH)+1);
//        }
//        List<String> st=new ArrayList<>();
//        st.add("A");
//        st.add("b");
//        System.out.println("---==="+st.size());
//        ClassLoader classLoader=ArticleApplication.class.getClassLoader();
//
//        File file=new File(Objects.requireNonNull(classLoader.getResource("serviceAccauntKey.json")).getFile());
//
//        FileInputStream serviceAccount=
//                new FileInputStream(file.getAbsolutePath());
//
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setDatabaseUrl("https://fir-crud-main-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .build();
//
//        FirebaseApp.initializeApp(options);



//        Set<String> zanId= ZoneId.getAvailableZoneIds();
//        for (String s : zanId) {
//            System.out.println(s);
//        }
        System.out.println("-----"+new Date().getTime());

    }

}

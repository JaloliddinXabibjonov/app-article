package com.example.article.component;

import com.example.article.entity.*;
import com.example.article.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PricesRepository pricesRepository;


    @Value("${spring.sql.init.mode}")
    private String mode;

    @Override
    public void run(String... args) {
        if (mode.equals("always")) {

            roleRepository.save(new Role(1, "ROLE_ADMINISTRATOR"));
            roleRepository.save(new Role(2, "ROLE_REDACTOR"));
            roleRepository.save(new Role(3, "ROLE_REVIEWER"));
            roleRepository.save(new Role(4, "ROLE_USER"));


            pricesRepository.save(new Prices(1,10000, 20000, 20000,25000));

            userRepository.save(
                    new User(
                            "Jaloliddin",
                            "Xabibjonov",
                            "+998993636224",
                            "Jaloliddin0292@gmail.com",
                            passwordEncoder.encode("+998993636224"),
                            roleRepository.findAllByIdIn(Collections.singletonList(1))
//                            Collections.singletonList(categoryRepository.getById(1))
                    ));
            userRepository.save(
                    new User(
                            "Azamxon ",
                            "Umarxonov",
                            "+998975431999",
                            "manager@",
                            passwordEncoder.encode("+998975431999"),
                            roleRepository.findAllByIdIn(Collections.singletonList(2))
//                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );
            userRepository.save(
                    new User(
                            "Akbar ",
                            "Qaxarjonov",
                            "+998903093028",
                            "Qaxarjonov@",
                            passwordEncoder.encode("+998903093028"),
                            roleRepository.findAllByIdIn(Collections.singletonList(3))
//                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );
            userRepository.save(
                    new User(
                            "Atham",
                            "Habibullayev",
                            "+998908124008" ,
                            "Atham@",
                            passwordEncoder.encode("+998908124008"),
                            roleRepository.findAllByIdIn(Collections.singletonList(3))
//                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );


            userRepository.save(
                    new User(
                            "Jasur",
                            "Quranboyev",
                            "+998998403157",
                            "Jasur@",
                            passwordEncoder.encode("+998998403157"),
                            roleRepository.findAllByIdIn(Collections.singletonList(4))
//                            Collections.singletonList(categoryRepository.getById(1))



                    )
            );

            userRepository.save(
                    new User(
                            "Kamol",
                            "Ahmadxajayev",
                            "+998994873405",
                            "Ahmadxajayev@",
                            passwordEncoder.encode("+998994873405"),
                            roleRepository.findAllByIdIn(Collections.singletonList(4))
//                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );
            userRepository.save(
                    new User(
                            "Anvar",
                            "Karimov",
                            "+998998332411",
                            "Anvar@gmail.com",
                            passwordEncoder.encode("+998998332411"),
                            roleRepository.findAllByIdIn(Collections.singletonList(1))
//                            Collections.singletonList(categoryRepository.getById(1))


                    ));

//            userRepository.save(
//                    new User(
//                            "Orif",
//                            "Orif",
//                            "6",
//                            "Orif@",
//                            passwordEncoder.encode("6"),
//                            roleRepository.findAllByIdIn(Collections.singletonList(1))
////                            Collections.singletonList(categoryRepository.getById(1))
//                    ));
//            articleRepository.save(new Article( "Tarix ", true,"dfds" ));
//            articleRepository.save(new Article( categoryRepository.getById(1), "Matematika ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Fizika ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Sanoat ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Informatika ", true,"dfds"));
//            articleRepository.save(new Article( categoryRepository.getById(1), "Axborot xavfsizligi ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Tarbiya ", true,"dfds"));
        }
    }
}
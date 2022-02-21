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
    ArticleRepository articleRepository;
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

            categoryRepository.save(new Category("informatika"));
            categoryRepository.save(new Category("fizika"));
            categoryRepository.save(new Category("matem"));
            categoryRepository.save(new Category("iqtisod"));

            pricesRepository.save(new Prices(1,5000));

            userRepository.save(
                    new User(
                            "admin",
                            "admin",
                            "1",
                            "admin",
                            passwordEncoder.encode("1"),
                            roleRepository.findAllByIdIn(Collections.singletonList(1)),
                            Collections.singletonList(categoryRepository.getById(1))
                    ));
            userRepository.save(
                    new User(
                            "Azamxon ",
                            "Umarxonov",
                            "2",
                            "manager@",
                            passwordEncoder.encode("2"),
                            roleRepository.findAllByIdIn(Collections.singletonList(2)),
                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );
            userRepository.save(
                    new User(
                            "Akbar ",
                            "Qaxarjonov",
                            "3",
                            "Qaxarjonov@",
                            passwordEncoder.encode("3"),
                            roleRepository.findAllByIdIn(Collections.singletonList(3)),
                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );
            userRepository.save(
                    new User(
                            "Atham ",
                            "Atham",
                            "7" ,
                            "Atham@",
                            passwordEncoder.encode("7"),
                            roleRepository.findAllByIdIn(Collections.singletonList(3)),
                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );


            userRepository.save(
                    new User(
                            "Jasur ",
                            "Jasur",
                            "8",
                            "Jasur@",
                            passwordEncoder.encode("8"),
                            roleRepository.findAllByIdIn(Collections.singletonList(3)),
                            Collections.singletonList(categoryRepository.getById(1))



                    )
            );

            userRepository.save(
                    new User(
                            "Kamol",
                            "Ahmadxajayev",
                            "4",
                            "Ahmadxajayev@",
                            passwordEncoder.encode("4"),
                            roleRepository.findAllByIdIn(Collections.singletonList(4)),
                            Collections.singletonList(categoryRepository.getById(1))
                    )
            );
            userRepository.save(
                    new User(
                            "Jaloliddin ",
                            "Jaloliddin",
                            "5",
                            "Jajoliddin@",

                            passwordEncoder.encode("5"),
                            roleRepository.findAllByIdIn(Collections.singletonList(4)),
                            Collections.singletonList(categoryRepository.getById(1))


                    ));

            userRepository.save(
                    new User(
                            "Orif",
                            "Orif",
                            "6",
                            "Orif@",
                            passwordEncoder.encode("6"),
                            roleRepository.findAllByIdIn(Collections.singletonList(1)),
                            Collections.singletonList(categoryRepository.getById(1))
                    ));
//            articleRepository.save(new Article( categoryRepository.getById(1), "Tarix ", true,"dfds" ));
//            articleRepository.save(new Article( categoryRepository.getById(1), "Matematika ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Fizika ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Sanoat ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Informatika ", true,"dfds"));
//            articleRepository.save(new Article( categoryRepository.getById(1), "Axborot xavfsizligi ", true,"dfds"));
//            articleRepository.save(new Article(categoryRepository.getById(1), "Tarbiya ", true,"dfds"));
        }
    }
}
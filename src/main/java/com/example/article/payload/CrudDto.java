package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CrudDto {
private UUID id ;
    private String fatherName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String workPlace;
    private String workExperience;
    private String languages;
    private String academicDegree;


}

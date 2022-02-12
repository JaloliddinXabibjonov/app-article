package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
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
    private Integer roleId;
    private List<Integer> role;
    private boolean active;
    private UUID photoId;
    private Set<Integer> categoryId;
}

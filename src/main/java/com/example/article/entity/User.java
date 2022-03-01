package com.example.article.entity;

import com.example.article.entity.template.AbsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AbsEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    private String phoneNumber;

//    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private boolean delete=false;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    private String fatherName;
    private String firstName;
    private String lastName;

    private  String firebaseToken;

//    @JsonIgnore
    private String email;

//    @JsonIgnore
@OneToMany
    private List<Attachment> photos;

//    @JsonIgnore
@ManyToMany
    private List<Category> categories;

    private String workPlace;
    private String workExperience;
    private String academicDegree;
    private String languages;

    private Integer code;

    @OneToMany
    private List<Attachment> scientificWork;

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;

    }

    public User(String phoneNumber, String password, List<Category> categoryID) {
        this.categories = categoryID;
        this.phoneNumber = phoneNumber;
        this.password = password;

    }

    public User(String firstName, String lastName, String phoneNumber, String email, String password, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.roles = roles;

    }

    public User(String firstName, String lastName, String phoneNumber, String email, String password, List<Role> roles, List<Category> categories) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.categories = categories;
    }

    public User(String firstName, String lastName, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }


    @JsonIgnore
    private boolean active;

    @JsonIgnore
    private boolean accountNonExpired = true;
    @JsonIgnore
    private boolean accountNonLocked = true;
    @JsonIgnore
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


}
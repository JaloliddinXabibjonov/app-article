package com.example.article.entity;

import java.util.HashSet;
import java.util.Set;

public class UserStorage {
    private static UserStorage instance;
    private Set<String> users;

    private UserStorage() {
        users = new HashSet<>();
    }


    public static synchronized UserStorage getInstance() {

        if (instance == null) {
            instance = new UserStorage();
        }

        return instance;
    }


    public Set<String> getUsers() {
        return users;
    }

    public void setUser(String userName) throws Exception {
        if (users.equals(userName)) {
            throw new Exception("user alerady with login username " + userName);
        }
        users.add(userName);
    }
}

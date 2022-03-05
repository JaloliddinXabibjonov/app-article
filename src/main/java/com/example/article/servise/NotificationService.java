package com.example.article.servise;

import com.example.article.entity.NotificationFromUser;
import com.example.article.entity.Role;
import com.example.article.entity.User;
import com.example.article.payload.ApiResponse;
import com.example.article.repository.NotificationFromUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationFromUserRepository fromUserRepository;

    public Integer notificationNumber(User user) {

        String roleName = null;
        for (Role role : user.getRoles()) {
            roleName = role.getRoleName();
        }
        Integer count = 0;
        if (roleName.equals("ROLE_ADMINISTRATOR")) {

            count = fromUserRepository.countAllByAdministratorIdAndReadFalse(user.getId());

        } else if (roleName.equals("ROLE_REVIEWER") || roleName.equals("ROLE_REDACTOR")) {

            count = fromUserRepository.countAllByUserIdAndReadFalse(user.getId());

        }
        return count;

    }


    public List<NotificationFromUser> notificationList(User user) {

        String roleName = null;
        for (Role role : user.getRoles()) {
            roleName = role.getRoleName();
        }
        List<NotificationFromUser> notificationList = null;

        if (roleName.equals("ROLE_ADMINISTRATOR")) {

            notificationList = fromUserRepository.findAllByAdministratorIdAndReadFalse(user.getId());

        } else if (roleName.equals("ROLE_REVIEWER") || roleName.equals("ROLE_REDACTOR")) {

            notificationList = fromUserRepository.findAllByUserIdAndReadFalse(user.getId());

        }
        return notificationList;

    }


    public ApiResponse deleteNotification(Integer notificationId){

        try {

            fromUserRepository.deleteById(notificationId);

        }catch (Exception e){
            return new ApiResponse("Notification o'chmadi");
        }
        return new ApiResponse("Notification o'chdi  urra ");
    }


}
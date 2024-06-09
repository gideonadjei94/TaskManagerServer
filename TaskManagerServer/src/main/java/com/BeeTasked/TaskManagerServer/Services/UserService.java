package com.BeeTasked.TaskManagerServer.Services;

import com.BeeTasked.TaskManagerServer.NoticeRepositoryImpl;
import com.BeeTasked.TaskManagerServer.Repository.NoticeRepository;
import com.BeeTasked.TaskManagerServer.Repository.UserRepository;
import com.BeeTasked.TaskManagerServer.collections.Notification;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private NoticeRepositoryImpl noticeCustom;

    //Admin  registration
    public User save(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if(userExists){
            ResponseEntity.status(400);
            System.out.println("User Already exists");
        }else{
        user.setAdmin(true);
        user.setTeamCode(generateTeamCode());
        userRepository.save(user);
            System.out.println("User Created");
        return user;
        }
        return null;
    }

    public Optional<User> authUser(User user) {
        boolean teamExists = userRepository.findByTeamCode(user.getTeamCode()).isPresent();
        if(teamExists && user.isActive()){
            ResponseEntity.ok("Welcome");
            return userRepository.findByTeamCode(user.getTeamCode());
        }else {
             ResponseEntity.status(400);
            System.out.println("Team does not exist");
        }
        return null;
    }

    private String generateTeamCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public List<User> getTeamList(User user) {
        if(user.isAdmin() && user.isActive()){
            return userRepository.findAllByTeamCode(user.getTeamCode());
        }else{
            ResponseEntity.status(400);
            System.out.println("Not permitted");
        }
    return null;
    }

    public List<Notification> getNotifications(User user, Notification notification) {
        String userId = user.getId();
        return noticeRepository.findByTeamAndIsReadNotContaining(notification.getTeam(), userId);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void markNotificationRead(User user, Notification notification, String isReadType) {
        if ("all".equals(isReadType)) {
            noticeCustom.updateMany(user);
        } else {
            noticeCustom.updateOne(notification, user);
        }
}
}

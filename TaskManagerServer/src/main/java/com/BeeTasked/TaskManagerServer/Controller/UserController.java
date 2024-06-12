package com.BeeTasked.TaskManagerServer.Controller;

import com.BeeTasked.TaskManagerServer.Services.UserService;
import com.BeeTasked.TaskManagerServer.collections.Notification;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user, user.getName(), user.getEmail());
    }

    @PostMapping("/login")
    public boolean loginUser(@RequestParam User user) {
        if(userService.authUser(user).isPresent()){
        userService.addUser(user, user.getName(), user.getEmail(), user.getTeamCode());
        }
        return userService.authUser(user).isPresent();
    }

    @GetMapping("/members")
    public List<User> getTeamList(@RequestParam User user){
        return userService.getTeamList(user);
    }

    @GetMapping
    public List<Notification> getNotificationsList(@RequestParam User user,Notification notification) {
        return userService.getNotifications(user, notification);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String _id, User user){
        userService.delete(_id, user);
    }

    @PutMapping("/updateProfile")
    public User updateUserProfile(@RequestBody User user, @RequestParam String id, @RequestParam boolean isAdmin) {
         id = user.getId();

        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent() && isAdmin) {
            User updateUser = existingUser.get();
            updateUser.setName(user.getName() != null ? user.getName() : updateUser.getName());
            updateUser.setRole(user.getRole() != null ? user.getRole() : updateUser.getRole());
            return userService.save(updateUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @PostMapping("/markRead")
    public void markNotificationRead(@RequestParam User user, @RequestParam Notification notification, @RequestParam String isReadType) {
        userService.markNotificationRead(user, notification, isReadType);
    }
    @PutMapping("/activate/{id}")
    public User activateUser(@PathVariable String _id, @RequestBody User user){
        return userService.activateUserProfile(_id, user);
    }

}

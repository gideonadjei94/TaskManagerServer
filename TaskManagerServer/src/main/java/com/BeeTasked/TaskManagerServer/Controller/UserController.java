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
        return userService.save(user);
    }

    @PostMapping("/login")
    public boolean loginUser(@RequestParam("teamCode") User user) {
        return userService.authUser(user).isPresent();
    }

    @GetMapping("/members")
    public List<User> getTeamList(@RequestParam("teamCode") User user){
        return userService.getTeamList(user);
    }

    @GetMapping
    public List<Notification> getNotificationsList(@RequestParam("id") User user,Notification notification) {
        return userService.getNotifications(user, notification);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.delete(id);
    }

    @PutMapping("/updateProfile")
    public User updateUserProfile(@RequestBody User user, @RequestParam String userId, @RequestParam boolean isAdmin) {
        String id = isAdmin ? user.getId() : userId;

        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
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
}

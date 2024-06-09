package com.BeeTasked.TaskManagerServer.Repository;

import com.BeeTasked.TaskManagerServer.collections.Notification;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.stereotype.Repository;


public interface NoticeRepositoryCustom {
    void updateMany(User user);
    void updateOne(Notification notification, User user);
}

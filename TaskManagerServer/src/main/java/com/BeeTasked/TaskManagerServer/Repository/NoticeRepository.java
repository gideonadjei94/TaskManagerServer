package com.BeeTasked.TaskManagerServer.Repository;

import com.BeeTasked.TaskManagerServer.collections.Notification;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends MongoRepository<Notification, String>, NoticeRepositoryCustom{
    List<Notification> findByTeamAndIsReadNotContaining(List<User> team, String userId);
}

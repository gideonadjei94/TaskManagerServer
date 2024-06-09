package com.BeeTasked.TaskManagerServer.Repository;

import com.BeeTasked.TaskManagerServer.Repository.NoticeRepositoryCustom;
import com.BeeTasked.TaskManagerServer.collections.Notification;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Repository
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateMany(User user) {
        Query query = new Query(Criteria.where("team").is(user.getId()).and("isRead").nin(user.getId()));
        Update update = new Update().push("isRead", user.getId());
        mongoTemplate.updateMulti(query, update, Notification.class);
    }

    @Override
    public void updateOne(Notification notification, User user) {
        Query query = new Query(Criteria.where("notification").is(notification.getId()).and("isRead").nin(user.getId()));
        Update update = new Update().push("isRead", user.getId());
        mongoTemplate.updateFirst(query, update, Notification.class);
    }
}

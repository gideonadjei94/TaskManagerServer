package com.BeeTasked.TaskManagerServer;

import com.BeeTasked.TaskManagerServer.Repository.NoticeRepositoryCustom;
import com.BeeTasked.TaskManagerServer.collections.Notification;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.util.Collections;

@Repository
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateMany(User user) {
        String userId = user.getId();
        Query query = new Query(Criteria.where("team").is(userId).and("isRead").nin(Collections.singletonList(userId)));
        Update update = new Update().push("isRead", userId);
        mongoTemplate.updateMulti(query, update, Notification.class);
    }

    @Override
    public void updateOne(Notification notification, User user) {

    }
}

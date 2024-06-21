package com.BeeTasked.TaskManagerServer.Repository;

import com.BeeTasked.TaskManagerServer.collections.Task;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String>, TaskRepositoryCustom {
    List<Task> findByIsTrashedFalse();
    List<Task> findByIsTrashedFalseAndTeamIn(List<User> team);

    void deleteByIsTrashed(boolean isTrashed);

}

package com.BeeTasked.TaskManagerServer.Repository;

import com.BeeTasked.TaskManagerServer.collections.Task;

import java.util.List;
import java.util.Map;

public interface TaskRepositoryCustom {
    List<Task> find(Map<String, Object> query);
    Task findTaskById(String id);
    void updateAllTrashedTasks(boolean isTrashed);
}

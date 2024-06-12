package com.BeeTasked.TaskManagerServer.Controller;

import com.BeeTasked.TaskManagerServer.Services.TaskService;
import com.BeeTasked.TaskManagerServer.collections.Task;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public Task createTask(@RequestBody Task task, User user) {
        return taskService.createTask(user,
                task.getTitle(),
                task.getTeam(),
                task.getStage(),
                task.getCreatedAt(),
                task.getPriority(),
                task.getAssets(),
                task.getActivities());
    }

    @PostMapping("/{id}/activity")
    public Task postTaskActivity(
            @PathVariable("id") String id,
            @RequestHeader("userId") User user,
            @RequestBody Task.Activity activity) {
        return taskService.postActivity(
                id,
                user,
                activity.getActivity()
        );
    }
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @PostMapping("/{id}/duplicate")
    public Task duplicateTask(@PathVariable String id) {
        return taskService.duplicateTask(id);
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable String id) {
        return taskService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) {
        taskService.deleteById(id);
    }
}

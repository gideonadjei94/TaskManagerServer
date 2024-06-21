package com.BeeTasked.TaskManagerServer.Controller;

import com.BeeTasked.TaskManagerServer.Services.TaskService;
import com.BeeTasked.TaskManagerServer.collections.Task;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/dashboard-statistics")
    public Map<String, Object> getDashboardStatistics(@RequestBody User user) {
        return taskService.getDashboardStatistics(user);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks( @RequestParam(required = false) String stage, @RequestParam(required = false, defaultValue = "false") boolean isTrashed)
    {
        try {
            List<Task> tasks = taskService.getTasks(stage, isTrashed);
            return tasks;
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable String id){
        try {
            Task task = taskService.getTask(id);
            return task;
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    @PostMapping("/tasks/{id}/subtasks")
    public void createSubTask(@PathVariable String id, @RequestBody Map<String, Object> requestBody){
        try {
            String title = (String) requestBody.get("title");
            String tag = (String) requestBody.get("tag");
            Date date = (Date) requestBody.get("date");

            taskService.createSubTask(id, title, tag, date);
            System.out.println("Subtask successfully added..");
        }catch (Exception e){
            e.getMessage();
        }
    }

    @PutMapping("/tasks/{id}")
    public void updateTask(@PathVariable String id, @RequestBody Map<String, Object> requestBody){
        try {
            String title = (String) requestBody.get("title");
            Date date = (Date) requestBody.get("date");
            List<User> team = (List<User>) requestBody.get("team");
            String stage = (String) requestBody.get("stage");
            String priority = (String) requestBody.get("priority");

            taskService.updateTask(id, title,date, team, stage, priority);
            System.out.println("Task Successfully updated..");
        }catch (Exception e){
            e.getMessage();
        }
    }

    @PutMapping("/tasks/{id}/trash")
    public void trashTask(@PathVariable String id){
        try {
            taskService.trashTask(id);
            System.out.println("Task trashed successfully");
        }catch (Exception e){
            e.getMessage();
        }
    }

    @DeleteMapping("/tasks/delete/{id}")
    public void deleteRestoreTask(@PathVariable String id, @RequestParam String actionType){
        try {
            taskService.deleteRestoreTask(id, actionType);
            System.out.println("Action performed successfully");
        }catch (Exception e){
            e.getMessage();
        }
    }

    @DeleteMapping("/tasks/delete")
    public void deleteRestoreAllTasks(@RequestParam String actionType){
        try {
            taskService.deleteRestoreTask(null, actionType);
            System.out.println("Operation performed successfully");
        }catch (Exception e){
            e.getMessage();
        }
    }
}

package com.BeeTasked.TaskManagerServer.Services;

import com.BeeTasked.TaskManagerServer.Repository.NoticeRepository;
import com.BeeTasked.TaskManagerServer.Repository.TaskRepository;
import com.BeeTasked.TaskManagerServer.Repository.UserRepository;
import com.BeeTasked.TaskManagerServer.collections.Notification;
import com.BeeTasked.TaskManagerServer.collections.Task;
import com.BeeTasked.TaskManagerServer.collections.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private NoticeRepository noticeRepository;

    public Task createTask(User user, String title, List<User> team, String stage, Date date, String priority, List<String> assets, List<Task.Activity> activities){
        String text = "New task has been assigned to you";
        if (team.size() > 1) {
            if(team.size()-1 == 1){
                text = text + " and  1 other person" ;
            }else{
            text = text + " and " + (team.size() - 1) + " others.";
            }
        }
        text = text + " The task priority is set a " + priority + " priority, so check and act accordingly. The task date is " + new Date(date.getTime()).toString() + ". Thank you!!!";

        Task task = new Task();
        task.setTitle(title);
        task.setTeam(team);
        task.setStage(stage.toLowerCase());
        task.setDate(date);
        task.setPriority(priority.toLowerCase());
        task.setAssets(assets);
        task.setActivities(activities);

        task = taskRepository.save(task);

        Notification notice = new Notification();
        notice.setTeam(team);
        notice.setText(text);
        notice.setTask(task);

        noticeRepository.save(notice);
        return task;
    }

    public Task duplicateTask(String id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        Task newTask = new Task();
        newTask.setTitle(task.getTitle() + " - Duplicate");
        newTask.setTeam(task.getTeam());
        newTask.setSubTasks(task.getSubTasks());
        newTask.setAssets(task.getAssets());
        newTask.setPriority(task.getPriority());
        newTask.setStage(task.getStage());
        newTask.setDate(task.getDate());

        newTask = taskRepository.save(newTask);
        String text = "New task has been assigned to you";
        if (task.getTeam().size() > 1) {
            if(task.getTeam().size()-1 == 1){
                text = text + " and  1 other person" ;
            }else{
                text = text + " and " + (task.getTeam().size() - 1) + " others.";
            }
        }
        text = text + " The task priority is set a " + task.getPriority() + " priority, so check and act accordingly. The task date is " + new Date(task.getDate().getTime()).toString() + ". Thank you!!!";
        Notification notice = new Notification();
        notice.setTeam(task.getTeam());
        notice.setText(text);
        notice.setTask(task);

        noticeRepository.save(notice);
        return newTask;
    }

    public Task postActivity(String taskId, User user, String activityDescription){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        Task.Activity activity = new Task.Activity();
        activity.setType(activity.getType());
        activity.setActivity(activityDescription);
        activity.setBy(user);

        task.getActivities().add(activity);
        taskRepository.save(task);

        return task;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    public void deleteById(String id) {
        taskRepository.deleteById(id);
    }
}

package com.BeeTasked.TaskManagerServer.collections;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "Tasks")
public class Task {
    @Id
    private String id;
    private String title;
    private Date date = new Date();
    private String priority = "normal";
    private String stage = "todo";
    private List<Activity> activities;
    private List<SubTask> subTasks;
    private List<String> assets;
    @DBRef
    private List<User> team;
    private Boolean isTrashed = false;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date updatedAt;

    public enum Priority {
        HIGH, MEDIUM, NORMAL, LOW
    }

    public enum Stage {
        TODO, IN_PROGRESS, COMPLETED
    }

    public enum ActivityType {
        ASSIGNED, STARTED, IN_PROGRESS, BUG, COMPLETED, COMMENTED
    }

    @Data
    @NoArgsConstructor
    public static class Activity {
        private ActivityType type = ActivityType.ASSIGNED;
        private String activity;
        private Date date = new Date();
        @DBRef
        private User by;
    }

    @Data
    @NoArgsConstructor
    public static class SubTask {
        private String title;
        private Date date = new Date();
        private String tag;
    }
}

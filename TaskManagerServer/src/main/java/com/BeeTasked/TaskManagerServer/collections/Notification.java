package com.BeeTasked.TaskManagerServer.collections;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
@Data
@Document(collection = "Notices")
public class Notification {
    @Id
    private String id;

    @DBRef
    private List<User> team;
    private String text;

    @DBRef
    private Task taks;
    private String notiType = "alert";

    public enum notiType {alert, message}

    @DBRef
    private List<User> isRead;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdAt;

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date updatedAt;
}

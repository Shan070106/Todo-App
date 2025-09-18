package com.toDo.model;
import java.time.LocalDateTime;

public class Todo {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime created_at;
    private LocalDateTime updeated_at;

    public Todo(){
        this.created_at = LocalDateTime.now();
        this.updeated_at = LocalDateTime.now();

    }

    public Todo(String title,String description){
        this();
        this.title = title;
        this.description = description;
    }

    public Todo(int id,String title,String description,boolean completed, LocalDateTime created_at,LocalDateTime updeated_at){
        this.title = title;
        this.completed = completed;
        this.description = description;
        this.created_at = created_at;
        this.updeated_at = updeated_at;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public LocalDateTime getUpdated_at() {
        return updeated_at;
    }
    public void setUpdeated_at(LocalDateTime updeated_at) {
        this.updeated_at = updeated_at;
    }
   

    
}

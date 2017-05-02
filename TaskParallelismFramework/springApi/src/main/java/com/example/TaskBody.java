package com.example;

import java.util.List;


public class TaskBody {
    String id;

    public String getIsTP() {
        return isTP;
    }

    public void setIsTP(String isTP) {
        this.isTP = isTP;
    }

    String isTP;
    List<Task> tasks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}

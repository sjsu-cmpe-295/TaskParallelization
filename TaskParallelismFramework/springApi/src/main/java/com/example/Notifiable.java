package com.example;

/**
 * Created by dmodh on 5/2/17.
 */
public interface Notifiable {
    public void onTaskCompletion(String taskId, String jsonOutput);
}

package com.example;


public class Task {
    private String id="";
    private String sensor="";
    private String time="";

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "id=" + id + "&sensor=" + sensor + "&time=" + time;
    }

}

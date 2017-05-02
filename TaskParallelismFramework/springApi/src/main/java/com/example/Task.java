package com.example;


public class Task {
    private String id="";
    private String sensor="";
    private String startTime="";
    private String endTime="";
    private String type="";

    public String getSensor() {
        return sensor;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "id=" + id + "&sensor=" + sensor + "&startTime=" + startTime + "&endTime=" + endTime + "&type=" + type;
    }

}

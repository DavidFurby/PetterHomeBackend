package com.backend.backend.Payload.request;

import java.util.List;

import com.backend.backend.Model.Schedule;

public class NeedRequest {
    private String type;
    private Boolean notified; 
    private List<Schedule> schedule;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Boolean getNotified() {
        return notified;
    }
    public void setNotified(Boolean notified) {
        this.notified = notified;
    }
    public List<Schedule> getSchedule() {
        return schedule;
    }
    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }
}

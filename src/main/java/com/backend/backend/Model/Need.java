package com.backend.backend.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Needs")
public class Need {

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    @NotBlank
    private String type;

    private Boolean notified;

    private List<Schedule> schedule = new ArrayList<>();

    public Need(String type, Boolean notified, List<Schedule> schedule) {
        ObjectId id;
        this.type = type;
        this.notified = notified;
        this.schedule = schedule;
    }

    public String getId() {
        return id.toHexString();
    }

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

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
    private final String type;

    private Boolean notified;

    private List <Schedule> schedule = new ArrayList<>(); 

    public Need(String type, Boolean notified, List <Schedule> schedule) {
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
    public Boolean getNotified() {
        return notified; 
    }
    public List<Schedule> getSchedule() {
        return schedule; 
    }
}

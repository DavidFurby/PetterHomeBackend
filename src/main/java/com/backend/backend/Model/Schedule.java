package com.backend.backend.Model;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Schedule")
public class Schedule {

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    @NotBlank
    private  String time;

    @NotBlank
    private String assignedUser;

    public Schedule(String time, String assignedUser) {
        ObjectId id;
        this.time = time;
        this.assignedUser = assignedUser;
    }

    public String getId() {
        return id.toHexString();
    }
    public String getTime() {
        return time;
    }
    public String getAssignedUser() {
        return assignedUser; 
    }
}

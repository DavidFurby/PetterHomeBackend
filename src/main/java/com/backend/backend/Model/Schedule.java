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
    private String assignedTo;

    public Schedule(String time, String assignedTo) {
        ObjectId id;
        this.time = time;
        this.assignedTo = assignedTo;
    }

    public String getId() {
        return id.toHexString();
    }
}

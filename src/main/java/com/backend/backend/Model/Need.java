package com.backend.backend.Model;

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

    private Schedule schedule; 

    public Need(String type, Boolean notified, Schedule schedule) {
        ObjectId id;
        this.type = type;
        this.notified = notified;
        this.schedule = schedule;
    }

    public String getId() {
        return id.toHexString();
    }
}

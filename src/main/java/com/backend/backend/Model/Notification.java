package com.backend.backend.Model;

import java.util.ArrayList;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notifications")

public class Notification {

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    @NotBlank
    public String petId;
    @NotBlank
    public String needId;
    @NotBlank
    public String scheduleId;
    @NotBlank
    public String assignedUserId;

    public Notification(@NotBlank String petId, String needId, String scheduleId) {
        ObjectId id;
        this.petId = petId;
        this.needId = needId;
        this.scheduleId = scheduleId;
        this.assignedUserId = assignedUserId;
    }

    public String getId() {
        return id.toHexString();
    }

    public String getPetId() {
        return petId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }
}

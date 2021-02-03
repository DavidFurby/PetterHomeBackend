package com.backend.backend.Model;


import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Invites")
public class Invite {

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    @NotBlank
    public String petId;
    @NotBlank
    public String userId;

    public Invite(@NotBlank String userId, String petId) {
        ObjectId id;

        this.userId = userId;
        this.petId = petId;
    }

    public String getId() {
        return id.toHexString();
    }

    public String getPetId() {
        return petId;
    }

    public String getUserId() {
        return userId;
    }
}

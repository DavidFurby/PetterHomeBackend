package com.backend.backend.Model;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;

public class InviteObject {
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    public String inviteId;
    @NotBlank
    public User user;
    @NotBlank
    public Pet pet;

    public InviteObject(@NotBlank String inviteId, User user, Pet pet) {
        ObjectId id;
        this.inviteId = inviteId;
        this.user = user;
        this.pet = pet;
    }

    public String getId() {
        return id.toHexString();
    }

    public String getInviteId() {
        return inviteId; 
    }

    public User getUser() {
        return user;
    }

    public Pet getPet() {
        return pet;
    }
}

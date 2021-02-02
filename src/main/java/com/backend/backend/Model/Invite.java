package com.backend.backend.Model;

import java.util.List;
import java.util.Optional;

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
	public Pet pet;
    @NotBlank
	public User user;

    public Invite(@NotBlank Pet pet, User user) {
        this.pet = pet;
        this.user = user;
    }

    public String getId() {
        return id.toHexString();
    }

    public Pet getPet() {
        return pet;
    }

    public User getUser() {
        return user;
    }
}

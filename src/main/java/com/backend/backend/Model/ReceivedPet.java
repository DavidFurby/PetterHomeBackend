package com.backend.backend.Model;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ReceivedPets")
public class ReceivedPet {
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    @NotBlank
    private Invite invite; 
    private Pet pet;
    private User user; 
    public ReceivedPet(@NotBlank Pet pet, User user) {
        ObjectId id;
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
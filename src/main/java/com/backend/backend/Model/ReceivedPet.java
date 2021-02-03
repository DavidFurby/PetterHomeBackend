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
    private String petId;
    @NotBlank
    private String senderId; 
    public ReceivedPet(@NotBlank String petId, String senderId) {
        ObjectId id;
        this.petId = petId;
        this.senderId = senderId;  
    }

    public String getId() {
        return id.toHexString();
    }

    public String getPetId() {
        return petId;
    }
    public String getUserId() {
        return senderId; 
    }
}
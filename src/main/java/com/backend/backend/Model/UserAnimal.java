package com.backend.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "Animals")
public class UserAnimal {

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    @NotBlank
    @Size(max = 20)
    private final String breed;

    @NotBlank
    private final String animal;

    public UserAnimal(String animal, String breed) {
        ObjectId id;
        this.animal = animal;
        this.breed = breed;
    }
    public String getId() {
        return id.toHexString();
    }
    public String getAnimal() {
        return animal;
    }
    public String getBreed() {
        return breed;
    }
}

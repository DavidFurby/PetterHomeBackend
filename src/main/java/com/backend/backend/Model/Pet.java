package com.backend.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "Pets")
public class Pet {

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setPetAge(Integer petAge) {
        this.petAge = petAge;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @NotBlank
    @Size(max = 20)
    private  String petName;

    @NotBlank
    private  Integer petAge;
    @NotBlank
    private  Gender gender;
    @NotBlank
    private  Animal animal;

    private Integer weight;

    private Integer height; 

    private Need need; 

    public Pet( String petName, Integer petAge, Gender gender, Animal animal, Integer weight, Integer height, Need need) {
        String newId;
        this.petName = petName;
        this.petAge = petAge;
        this.gender = gender;
        this.animal = animal;
        this.weight = weight;
        this.height = height; 
        this.need = need; 
    }
    public String getId() {
        return id.toHexString();
    }
    public String getPetName() {
        return petName;
    }
    public Integer getPetAge() {
        return petAge;
    }
    public Animal getAnimal() {
        return animal;
    }
    public Gender getGender() {
        return gender;
    }

    public void setPetName() {
    }
    public Need getNeed() {
        return need; 
    }
    public Integer getWeight() {
        return weight; 
    }
    public Integer getHeight() {
        return height; 
    }
}

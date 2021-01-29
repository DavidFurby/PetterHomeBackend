package com.backend.backend.Payload.request;

import com.backend.backend.Model.Animal;
import com.backend.backend.Model.Gender;
import com.backend.backend.Model.Need;

public class PetRequest {
    private String petName;
    private Integer petAge;
    private Animal animal;
    private Gender gender;
    private Need need;
    private Integer height;
    private Integer weight;

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Integer getPetAge() {
        return petAge;
    }

    public void setPetAge(Integer petAge) {
        this.petAge = petAge;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }
    
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
}

package com.backend.backend.Payload.request;

import com.backend.backend.Model.Animal;
import com.backend.backend.Model.Gender;

public class PetRequest {
    private String petName;
    private Integer petAge;
    private Animal animal;
    private Gender gender;

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

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
}

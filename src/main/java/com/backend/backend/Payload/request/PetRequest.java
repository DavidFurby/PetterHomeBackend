package com.backend.backend.Payload.request;

import java.util.List;

import com.backend.backend.Model.UserAnimal;
import com.backend.backend.Model.Gender;
import com.backend.backend.Model.Need;

public class PetRequest {
    private String petName;
    private Integer petAge;
    private UserAnimal animal;
    private Gender gender;
    private List<Need> needs;
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

    public UserAnimal getAnimal() {
        return animal;
    }

    public List<Need> getNeed() {
        return needs;
    }

    public void setNeed(List<Need> needs) {
        this.needs = needs;
    }

    public void setAnimal(UserAnimal animal) {
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

	public List<Need> getNeeds() {
		return needs;
	}
}

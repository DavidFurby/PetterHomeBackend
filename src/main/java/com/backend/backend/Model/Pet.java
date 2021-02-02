package com.backend.backend.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "Pets")
public class Pet {

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();

    @NotBlank
    @Size(max = 20)
    private String petName;

    @NotBlank
    private Integer petAge;
    @NotBlank
    private Gender gender;
    @NotBlank
    private UserAnimal animal;

    private Integer weight;

    private Integer height;

    private List<Need> needs = new ArrayList<>();
    private List<User> sharedWith = new ArrayList<>();

    public Pet(String petName, Integer petAge, Gender gender, UserAnimal animal, Integer weight, Integer height) {
        this.petName = petName;
        this.petAge = petAge;
        this.gender = gender;
        this.animal = animal;
        this.weight = weight;
        this.height = height;
        this.needs = needs;
        this.sharedWith = sharedWith;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setPetAge(Integer petAge) {
        this.petAge = petAge;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAnimal(UserAnimal animal) {
        this.animal = animal;
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

    public UserAnimal getAnimal() {
        return animal;
    }

    public Gender getGender() {
        return gender;
    }

    public void setPetName() {
    }

    public List<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(List<Need> needs) {
        this.needs = needs;
    }

    public Object addNeed(Need need) {
        return needs.add(need);
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getHeight() {
        return height;
    }

    public List<User> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<User> sharedWith) {
        this.sharedWith = sharedWith;
    }

	public Object addSharedWith(User user) {
		return sharedWith.add(user);
	}

	public void setWeight(Integer weight2) {
	}

	public void setHeight(Integer height2) {
	}
}

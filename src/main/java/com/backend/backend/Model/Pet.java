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
    private Animal animal;

    private Integer weight;

    private Integer height;

    private List<Need> needs = new ArrayList<>();

    public Pet(String petName, Integer petAge, Gender gender, Animal animal, Integer weight, Integer height) {
        String newId;
        this.petName = petName;
        this.petAge = petAge;
        this.gender = gender;
        this.animal = animal;
        this.weight = weight;
        this.height = height;
        this.needs = needs;
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

    public void setAnimal(Animal animal) {
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

    public Animal getAnimal() {
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
    public Integer getWeight() {
        return weight;
    }

    public Integer getHeight() {
        return height;
    }

	public Object addNeed(Need need) {
		return needs.add(need);
	}

}

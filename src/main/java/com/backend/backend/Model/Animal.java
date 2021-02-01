package com.backend.backend.Model;

import java.sql.Array;
import java.util.ArrayList;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Animals")
public class Animal {
    @Id
    private String id;

    private String animal;
    private ArrayList<Object> breeds;

    public Animal(String animal, ArrayList<Object> breeds) {
        this.animal = animal;
        this.breeds = breeds;
    }

    public String getId() {
        return id;
    }

    public String getAnimal() {
        return animal;
    }
    public ArrayList<Object> getBreeds() {
        return breeds; 
    }
}

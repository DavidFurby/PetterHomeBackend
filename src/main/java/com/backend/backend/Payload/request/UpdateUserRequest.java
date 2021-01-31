package com.backend.backend.Payload.request;

import com.backend.backend.Model.Pet;

import java.util.List;

public class UpdateUserRequest {
    public String id;
    private List<Pet> pets;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Pet> getPets() {
        return pets;
    }
}

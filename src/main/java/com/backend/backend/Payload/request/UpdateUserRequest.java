package com.backend.backend.Payload.request;

import com.backend.backend.Model.Pet;

import java.util.ArrayList;

public class UpdateUserRequest {
    public String id;
    private ArrayList<Pet> pets;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setPets(ArrayList<Pet> pets) {
        this.pets = pets;
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }
}

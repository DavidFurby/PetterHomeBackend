package com.backend.backend.Payload.request;

import com.backend.backend.Model.Pet;
import com.backend.backend.Model.User;

public class InviteRequest {
    private Pet pet;
    private User user;

    public Pet getPet() {
        return pet;
    }
    public void setType(Pet pet) {
        this.pet = pet;
    }
	public User getUser() {
		return user;
	}
  
}

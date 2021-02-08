package com.backend.backend.Model;

import javax.validation.constraints.NotBlank;

public class PetMessageObject {
    public Pet pet;
    @NotBlank
    public String msg;


    public PetMessageObject(@NotBlank Pet pet, String msg) {
        this.pet = pet;
        this.msg = msg;
    }

}

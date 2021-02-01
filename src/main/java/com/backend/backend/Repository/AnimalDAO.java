package com.backend.backend.Repository;

import java.util.Collection;

import com.backend.backend.Model.Animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalDAO {


    @Autowired
    private static AnimalRepository repository;

    public static Collection<Animal> getAnimals() {
        return repository.findAll();
    }
}


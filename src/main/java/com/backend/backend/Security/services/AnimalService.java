package com.backend.backend.Security.services;

import java.util.List;

import com.backend.backend.Model.Animal;
import com.backend.backend.Repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 

@Service
public class AnimalService {

    @Autowired
    AnimalRepository respository;

    public List<Animal> getAll() {
        return respository.findAll();
	}
}

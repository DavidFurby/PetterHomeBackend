package com.backend.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.backend.backend.Model.Animal;
public interface AnimalRepository extends MongoRepository<Animal, String> {

}

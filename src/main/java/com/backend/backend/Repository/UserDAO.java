package com.backend.backend.Repository;

import com.backend.backend.Model.Pet;
import com.backend.backend.Model.User;
import com.backend.backend.Payload.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDAO {

    @Autowired
    private UserRepository repository;
    public Optional<User> getCurrentUser(String id) {
        return repository.findById(id);
    }

    public Optional<User> insertPet(Pet pet, String id) {
       Optional<User> user = repository.findById(id);
        return user;
    }
}

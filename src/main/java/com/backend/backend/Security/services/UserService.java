package com.backend.backend.Security.services;

import java.util.Collection;
import java.util.List;

import com.backend.backend.Model.User;
import com.backend.backend.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public List<User> getAll() {
        return repository.findAll();
	}
}

package com.backend.backend.Repository;

import com.backend.backend.Model.ERole;
import com.backend.backend.Model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}

package com.backend.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Roles")
public class Role {
    @Id
    private String id;

    private final ERole name;


    public Role(ERole name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public ERole getName() {
        return name;
    }

}
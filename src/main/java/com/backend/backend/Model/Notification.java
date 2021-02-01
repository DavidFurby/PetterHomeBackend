package com.backend.backend.Model;

import java.util.ArrayList;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Notifications")

public class Notification {
    

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    private ObjectId id = new ObjectId();
    
    private ArrayList<Notification> pets; 
    public Notification() {
        this.pets = pets; 
    }
}

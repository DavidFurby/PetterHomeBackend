package com.backend.backend.Repository;

import com.backend.backend.Model.Notification;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    
}

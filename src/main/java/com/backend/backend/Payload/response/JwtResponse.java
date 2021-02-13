package com.backend.backend.Payload.response;

import com.backend.backend.Model.Invite;
import com.backend.backend.Model.Notification;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.ReceivedPet;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private final List<String> roles;
    private final List<Pet> pets;
    private final List<String> notifications;
    private final List<Invite> invites;
    private final List<ReceivedPet> receivedPets;

    public JwtResponse(String accessToken, String id, String username, String email, List<String> roles, List<Pet> pets,
            List<String> notifications, List<Invite> invites, List<ReceivedPet> receivedPets) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.pets = pets;
        this.notifications = notifications;
        this.invites = invites;
        this.receivedPets = receivedPets;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public List<ReceivedPet> getReceivedPets() {
        return receivedPets;
    }

}

package com.backend.backend.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "Users")
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private final String email;
    @NotBlank
    @Size(max = 120)
    private String password;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public List<Pet> pets;
    public List<Notification> notifications;
    public List<Invite> invites;
    public List<ReceivedPet> receivedPets; 

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pets = pets;
        this.notifications = notifications;
        this.invites = invites;
        this.receivedPets = receivedPets;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public void addPet(Pet newPet) {
        pets.add(newPet);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notification) {
        this.notifications = notification;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public Object addNotification(Notification notification) {
        return notifications.add(notification);
    }

    public void setInvites(List<Invite> removeInvite) {
        this.invites = removeInvite;
    }

    public Object addInvite(Invite invite) {
        return invites.add(invite);
    }

	public void setReceivedPets(List<ReceivedPet> filterReceivedPets) {
        this.receivedPets = filterReceivedPets; 
	}

	public List<ReceivedPet> getReceivedPets() {
		return receivedPets;
	}
    public Object receivePet(ReceivedPet receivedPet) {
		return receivedPets.add(receivedPet);
	}
}
package com.backend.backend.Security.services;

import com.backend.backend.Model.Invite;
import com.backend.backend.Model.Notification;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.ReceivedPet;
import com.backend.backend.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final String id;

    private final String username;

    private final String email;

    @JsonIgnore
    private String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final List<Pet> pets;

    private final List<String> notifications;

    private final List<Invite> invites;
    private final List<ReceivedPet> receivedPets;

    public UserDetailsImpl(String id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities, List<Pet> pets, List<String> notifications,
            List<Invite> invites, List<ReceivedPet> receivedPets) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.pets = pets;
        this.notifications = notifications;
        this.invites = invites;
        this.receivedPets = receivedPets;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        List<Pet> pets = user.getPets();
        List<String> notifications = user.getNotifications();
        List<Invite> invites = user.getInvites();
        List<ReceivedPet> receivedPets = user.getReceivedPets(); 
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities,
                pets, notifications, invites, receivedPets);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public List<Pet> getPets() {
        return pets;
    }

    public String setPassword(String password) {
        return this.password = password;
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
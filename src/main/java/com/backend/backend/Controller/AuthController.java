package com.backend.backend.Controller;

import com.backend.backend.Model.Pet;
import com.backend.backend.Model.User;
import javax.validation.Valid;

import com.backend.backend.Model.ReceivedPet;
import com.backend.backend.Model.ERole;
import com.backend.backend.Model.Invite;
import com.backend.backend.Model.Notification;
import com.backend.backend.Model.Role;
import com.backend.backend.Payload.request.LoginRequest;
import com.backend.backend.Payload.request.SignupRequest;
import com.backend.backend.Payload.request.ChangePasswordRequest;
import com.backend.backend.Payload.response.JwtResponse;
import com.backend.backend.Payload.response.MessageResponse;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Security.jwt.JwtUtils;
import com.backend.backend.Security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles, userDetails.getPets(),
                        userDetails.getNotifications(), userDetails.getInvites()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        } if(signUpRequest.getUsername() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: A username must be selected!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        if(signUpRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: An email must be selected"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRoles();
        ArrayList<Pet> pets = new ArrayList<>();
        Set<Role> roles = new HashSet<>();
        ArrayList<Notification> notifications = new ArrayList<>();
        ArrayList<Invite> invites = new ArrayList<>(); 
        ArrayList<ReceivedPet> acceptedPets = new ArrayList<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setPets(pets);
        user.setInvites(invites);
        user.setNotifications(notifications);
        user.setAcceptedPets(acceptedPets);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PutMapping("/recoverPassword")
    public ResponseEntity<?> recoverPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(changePasswordRequest.getUsername(), changePasswordRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String newPassword = changePasswordRequest.getNewPassword();      
        Optional<User> user = userRepository.findByUsername(changePasswordRequest.getUsername());
        user.ifPresent(b -> b.setPassword(encoder.encode(newPassword)));
        user.ifPresent(b -> userRepository.save(b));
        return ResponseEntity.ok(new MessageResponse("password changed successfully!"));
    
    }
    @GetMapping("/test")
    public String test() {
        return "test"; 
    }


}

package com.backend.backend.Controller;

import com.backend.backend.Model.Need;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.User;
import com.backend.backend.Payload.request.NeedRequest;
import com.backend.backend.Payload.request.PetRequest;
import com.backend.backend.Payload.response.MessageResponse;
import com.backend.backend.Repository.UserRepository;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getCurrentUser")
    @PreAuthorize("hasRole('USER')")
    public Optional<User> getCurrentUser(@RequestParam String id) {
        return userRepository.findById(id);
    }

    @PostMapping("/addPetToUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> addPetToUser(@Valid @RequestBody PetRequest petRequest,
            @RequestParam String userId) {

        Optional<User> user = userRepository.findById(userId);
        if (petRequest.getPetName() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: A petname must be selected"));
        }
        if (petRequest.getAnimal() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: An animal must be selected"));
        }
        Pet pet = new Pet(petRequest.getPetName(), petRequest.getPetAge(), petRequest.getGender(),
                petRequest.getAnimal(), petRequest.getHeight(), petRequest.getWeight());
        List<Need> needs = new ArrayList<>();
        user.ifPresent(u -> u.addPet(pet));
        user.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Pet added successfully!"));
    }

    @PostMapping("/addNeedToPet")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> addNeedToPet(@Valid @RequestBody NeedRequest needRequest,
            @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> user = userRepository.findById(userId);
        
        ArrayList<Pet> pets = user.get().pets;

        List<Pet> filterPets = pets.stream().filter(p -> p.getId() == petId).collect(Collectors.toList()); 
        Pet setPet = filterPets.get(0);

        if (needRequest.getType() == null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: A name for the pets need must be selected"));
        }
        if (needRequest.getSchedule() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No schedule has been made"));
        }
        Need need = new Need(needRequest.getType(), needRequest.getNotified(), needRequest.getSchedule());

        return ResponseEntity.ok(new MessageResponse("Pets need added successfully!"));
    }

    @GetMapping("/getPetById")
    @PreAuthorize("hasRole('USER')")
    public Pet getPetById(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> user = userRepository.findById(userId);
        ArrayList<Pet> pets = user.get().pets;

        for (Pet pet : pets) {
            if (pet.getId().equals(petId)) {
                return pet;
            }
        }
        return null;
    }

    @PutMapping("/updateUserPet")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> updateUserPet(@Valid @RequestBody PetRequest petRequest,
            @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> user = userRepository.findById(userId);

        return ResponseEntity.ok(new MessageResponse("The pets information has been updated successfully!"));
    }

    @DeleteMapping("/deletePetFromUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> deletePetFromUser(@Valid @RequestBody PetRequest petRequest,
            @RequestParam String petId) {

        return ResponseEntity.ok(new MessageResponse("Pet has been deleted! :( "));
    }
}
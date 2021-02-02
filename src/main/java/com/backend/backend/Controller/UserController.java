package com.backend.backend.Controller;

import com.backend.backend.Model.ReceivedPet;
import com.backend.backend.Model.Animal;
import com.backend.backend.Model.Invite;
import com.backend.backend.Model.Need;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.User;
import com.backend.backend.Payload.request.ChangePasswordRequest;
import com.backend.backend.Payload.request.InviteRequest;
import com.backend.backend.Payload.request.NeedRequest;
import com.backend.backend.Payload.request.PetRequest;
import com.backend.backend.Payload.response.MessageResponse;

import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.services.AnimalService;
import com.backend.backend.Security.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private UserService userService;

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
        user.ifPresent(u -> u.addPet(pet));
        user.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Pet added successfully!"));
    }

    @PostMapping("/addNeedToPet")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> addNeedToPet(@Valid @RequestBody NeedRequest needRequest,
            @RequestParam Map<String, String> id) {
        if (needRequest.getType() == null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: A name for the pets need must be selected"));
        }
        if (needRequest.getSchedule() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No schedule has been made"));
        }
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> user = userRepository.findById(userId);

        List<Pet> pets = user.get().pets;

        List<Pet> filterPets = pets.stream().filter(p -> p.getId().equals(petId)).collect(Collectors.toList());
        Optional<Pet> setPet = filterPets.stream().findFirst();

        Need need = new Need(needRequest.getType(), needRequest.getNotified(), needRequest.getSchedule());
        setPet.ifPresent(p -> p.addNeed(need));
        user.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Pets need added successfully!"));
    }

    @GetMapping("/getPetById")
    @PreAuthorize("hasRole('USER')")
    public Pet getPetById(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> user = userRepository.findById(userId);
        List<Pet> pets = user.get().pets;

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
        List<Pet> pets = user.get().pets;
        List<Pet> updatedPets = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getId().equals(petId)) {
                pet.setPetName(petRequest.getPetName());
                pet.setPetAge(petRequest.getPetAge());
                pet.setGender(petRequest.getGender());
                pet.setWeight(petRequest.getWeight());
                pet.setHeight(petRequest.getHeight());
                updatedPets.add(pet);
            } else {
                updatedPets.add(pet);
            }
        }

        user.ifPresent(u -> u.setPets(updatedPets));
        user.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("The pets information has been updated successfully!"));
    }

    @PutMapping("/updateUserSchedule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> updateUserSchedule(@Valid @RequestBody NeedRequest needRequest,
            @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        String needId = id.get("needId");
        Optional<User> user = userRepository.findById(userId);
        List<Pet> pets = user.get().pets;
        List<Pet> updatedPets = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getId().equals(petId)) {
                updatedPets.add(pet);
            }
        }
        Optional<Pet> petFilter = updatedPets.stream().findFirst();
        Pet realPet = petFilter.get();
        List<Need> petNeeds = realPet.getNeeds();
        List<Need> updatedNeeds = new ArrayList<>();
        for (Need need : petNeeds) {
            if (need.getId().equals(needId)) {
                need.setType(needRequest.getType());
                need.setNotified(needRequest.getNotified());
                need.setSchedule(needRequest.getSchedule());
                updatedNeeds.add(need);
            }
        }
        realPet.setNeeds(updatedNeeds);
        for (Pet pet : pets) {
            if (pet.getId().equals(realPet.getId())) {
                pet = realPet;
            }
        }
        user.ifPresent(u -> u.setPets(pets));
        user.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("The pets information has been updated successfully!"));
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest, @RequestParam String userId) {

        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> u.getPassword());
        user.ifPresent(u -> u.setPassword(encoder.encode(changePasswordRequest.getNewPassword())));
        user.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Password has been changed successfully!"));
    }

    @DeleteMapping("/deletePetFromUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> deletePetFromUser(@Valid @RequestBody PetRequest petRequest,
            @RequestParam String petId) {

        return ResponseEntity.ok(new MessageResponse("Pet has been deleted! :( "));
    }

    @PostMapping("/sendInvite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> sendInvite(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        String receiverId = id.get("receiverId");

        Optional<User> sender = userRepository.findById(userId);
        Optional<User> receiver = userRepository.findById(receiverId);
        List<Pet> pets = sender.get().pets;

        List<Pet> filterPets = pets.stream().filter(p -> p.getId().equals(petId)).collect(Collectors.toList());
        Optional<Pet> setPet = filterPets.stream().findFirst();

        Pet getPet = setPet.get();
        User getSender = sender.get();

        Invite invite = new Invite(getPet, getSender);
        receiver.ifPresent(u -> u.addInvite(invite));
        receiver.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Invite has been sent!"));
    }

    @PostMapping("/acceptInvite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> acceptInvite(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String inviteId = id.get("inviteId");

        Optional<User> receiver = userRepository.findById(userId);
        User user = receiver.get();
        List<Invite> invites = receiver.get().invites;
        List<Invite> filterInvites = invites.stream().filter(i -> i.getId().equals(inviteId))
                .collect(Collectors.toList());
        Optional<Invite> setInvite = filterInvites.stream().findFirst();

        Pet receivedPet = setInvite.get().pet;
        User sender = setInvite.get().user;
        Optional<User> opSender = userRepository.findById(sender.getId());
        ReceivedPet acceptedPet = new ReceivedPet(receivedPet, sender);
        List<Pet> senderPets = sender.getPets();

        List<Pet> updatePets = new ArrayList<>();
        for (Pet pet : senderPets) {
            if (pet.getId().equals(receivedPet.getId())) {
                pet.addSharedWith(user);
                updatePets.add(pet);
            } else {
                updatePets.add(pet);

            }
        }
        receiver.ifPresent(u -> receivedPet.addSharedWith(u));

        opSender.ifPresent(u -> u.setPets(updatePets));
        opSender.ifPresent(u -> userRepository.save(u));

        receiver.ifPresent(u -> u.acceptPet(acceptedPet));
        List<Invite> removeInvite = invites.stream().filter(i -> !i.getId().equals(inviteId))
                .collect(Collectors.toList());
        receiver.ifPresent(u -> u.setInvites(removeInvite));
        receiver.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Invite has been accepted!"));
    }

    @GetMapping("/getAllAnimals")
    @PreAuthorize("hasRole('USER')")
    public Collection<Animal> getAllAnimals() {
        return animalService.getAll();
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('USER')")
    public Collection<User> getAllUsers() {
        return userService.getAll();
    }
}
package com.backend.backend.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.backend.backend.Model.MessageObject;
import com.backend.backend.Model.Need;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.PetMessageObject;
import com.backend.backend.Model.Schedule;
import com.backend.backend.Model.User;
import com.backend.backend.Payload.request.NeedRequest;
import com.backend.backend.Payload.request.PetRequest;
import com.backend.backend.Payload.response.MessageResponse;
import com.backend.backend.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class PetController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAllPets")
    @PreAuthorize("hasRole('USER')")
    public List<Pet> getAllPets(@RequestParam String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<Pet> pets = user.getPets();
        return pets;
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

    @PostMapping("/addPetToUser")
    @PreAuthorize("hasRole('USER')")
    public PetMessageObject addPetToUser(@Valid @RequestBody PetRequest petRequest, @RequestParam String userId) {

        Optional<User> user = userRepository.findById(userId);

        Pet pet = new Pet(petRequest.getPetName(), petRequest.getPetAge(), petRequest.getGender(),
                petRequest.getAnimal(), petRequest.getHeight(), petRequest.getWeight());
        user.ifPresent(u -> u.addPet(pet));
        user.ifPresent(u -> userRepository.save(u));
        String message = "pet was added successfully";
        PetMessageObject petMessageObject = new PetMessageObject(pet, message);
        return petMessageObject;
    }

    @DeleteMapping("/deleteNeed")
    @PreAuthorize("hasRole('USER')")
    public MessageObject deleteNeed(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        String needId = id.get("needId");
        Optional<User> opUser = userRepository.findById(userId);
        User realUser = opUser.get();
        List<Pet> pets = realUser.getPets();
        List<Pet> filterPets = pets.stream().filter(p -> p.getId().equals(petId)).collect(Collectors.toList());
        Optional<Pet> opPet = filterPets.stream().findFirst();
        Pet realPet = opPet.get();
        List<Need> needs = realPet.getNeeds();
        List<Need> filterNeeds = needs.stream().filter(n -> !n.getId().equals(needId)).collect(Collectors.toList());

        opPet.ifPresent(p -> p.setNeeds(filterNeeds));
        opUser.ifPresent(u -> u.setPets(filterPets));
        opUser.ifPresent(u -> userRepository.save(u));
        String message = "need has been deleted successfully";
        MessageObject object = new MessageObject(needId, message);
        return object;
    }

    @DeleteMapping("/deleteSchedule")
    @PreAuthorize("hasRole('USER')")
    public MessageObject deleteSchedule(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        String needId = id.get("needId");
        String scheduleId = id.get("scheduleId");

        Optional<User> opUser = userRepository.findById(userId);
        User realUser = opUser.get();
        List<Pet> pets = realUser.getPets();
        List<Pet> filterPets = pets.stream().filter(p -> p.getId().equals(petId)).collect(Collectors.toList());
        Optional<Pet> opPet = filterPets.stream().findFirst();
        Pet realPet = opPet.get();
        List<Need> needs = realPet.getNeeds();
        List<Need> filterNeeds = needs.stream().filter(n -> n.getId().equals(needId)).collect(Collectors.toList());
        Optional<Need> opNeed = filterNeeds.stream().findFirst();
        Need realNeed = opNeed.get();
        List<Schedule> schedules = realNeed.getSchedules();
        List<Schedule> filterSchedule = schedules.stream().filter(s -> !s.getId().equals(scheduleId))
                .collect(Collectors.toList());
        opNeed.ifPresent(n -> n.setSchedules(filterSchedule));
        opPet.ifPresent(p -> p.setNeeds(filterNeeds));
        opUser.ifPresent(u -> u.setPets(filterPets));
        opUser.ifPresent(u -> userRepository.save(u));
        String message = "schedule has been deleted successfully";
        MessageObject object = new MessageObject(scheduleId, message);
        return object;
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
    public PetMessageObject updateUserPet(@Valid @RequestBody PetRequest petRequest,
            @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> user = userRepository.findById(userId);
        List<Pet> pets = user.get().pets;
        List<Pet> updatedPets = new ArrayList<>();
        PetMessageObject petMessage = null;
        for (Pet pet : pets) {
            if (pet.getId().equals(petId)) {
                pet.setPetName(petRequest.getPetName());
                pet.setPetAge(petRequest.getPetAge());
                pet.setGender(petRequest.getGender());
                pet.setWeight(petRequest.getWeight());
                pet.setHeight(petRequest.getHeight());
                pet.setAnimal(petRequest.getAnimal());
                updatedPets.add(pet);
                String msg = "The pets information has been updated successfully!";
                petMessage = new PetMessageObject(pet, msg);
            } else {
                updatedPets.add(pet);
            }
        }

        user.ifPresent(u -> u.setPets(updatedPets));
        user.ifPresent(u -> userRepository.save(u));

        return petMessage;
    }

    @DeleteMapping("/deletePetFromUser")
    @PreAuthorize("hasRole('USER')")
    public MessageObject deletePetFromUser(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> opUser = userRepository.findById(userId);
        User realUser = opUser.get();
        List<Pet> pets = realUser.getPets();
        List<Pet> filterPets = pets.stream().filter(p -> !p.getId().equals(petId)).collect(Collectors.toList());
        /*
         * List<User> allUsers = userRepository.findAll(); for (User listUser :
         * allUsers) { List<Invite> listInvites = listUser.getInvites(); List<Invite>
         * filterInvites = new ArrayList<>(); List<ReceivedPet> listReceivedPets =
         * listUser.getReceivedPets(); List<ReceivedPet> filterReceivedPets = new
         * ArrayList<>(); for (Invite listInvite : listInvites) { if
         * (!listInvite.getPetId().equals(petId)) { filterInvites.add(listInvite); } }
         * for (ReceivedPet listReceivedPet : listReceivedPets) { if
         * (!listReceivedPet.getPetId().equals(petId)) {
         * filterReceivedPets.add(listReceivedPet); } }
         * listUser.setInvites(filterInvites);
         * listUser.setReceivedPets(filterReceivedPets); }
         */

        opUser.ifPresent(u -> u.setPets(filterPets));
        opUser.ifPresent(u -> userRepository.save(u));
        String message = "pet has been deleted successfully";
        MessageObject object = new MessageObject(petId, message);
        return object;
    }
}

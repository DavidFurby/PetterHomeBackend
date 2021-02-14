package com.backend.backend.Controller;

import com.backend.backend.Model.ReceivedPet;
import com.backend.backend.Model.Schedule;
import com.backend.backend.Model.Animal;
import com.backend.backend.Model.Invite;
import com.backend.backend.Model.InviteObject;
import com.backend.backend.Model.MessageObject;
import com.backend.backend.Model.Need;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.User;
import com.backend.backend.Payload.request.ChangePasswordRequest;
import com.backend.backend.Payload.request.InviteRequest;
import com.backend.backend.Payload.request.NeedRequest;
import com.backend.backend.Payload.request.ScheduleRequest;
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

import java.util.*;
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
    public Optional<User> getCurrentUser(@RequestParam String userId) {
        return userRepository.findById(userId);
    }

    @PostMapping("/addScheduleToNeed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> addScheduleToNeed(@Valid @RequestBody ScheduleRequest scheduleRequest,
            @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        String needId = id.get("needId");
        Optional<User> user = userRepository.findById(userId);

        List<Pet> pets = user.get().pets;

        List<Pet> filterPets = pets.stream().filter(p -> p.getId().equals(petId)).collect(Collectors.toList());
        Optional<Pet> setPet = filterPets.stream().findFirst();
        Pet realPet = setPet.get();
        List<Need> needs = realPet.getNeeds();
        List<Need> filterNeeds = new ArrayList<>();
        for (Need need : needs) {
            if (need.getId().equals(needId)) {
                filterNeeds.add(need);
            }
        }
        Optional<Need> correctNeed = filterNeeds.stream().findFirst();
        Need realNeed = correctNeed.get();
        List<Schedule> schedules = realNeed.getSchedules();
        Schedule schedule = new Schedule(scheduleRequest.getTime(), scheduleRequest.getAssignedUser());
        schedules.add(schedule);
        realNeed.setSchedules(schedules);
        for (Need tempNeed : needs) {
            if (tempNeed.getId().equals(realNeed.getId())) {
                tempNeed.equals(realNeed);
            }
        }
        realPet.setNeeds(needs);
        for (Pet tempPet : pets) {
            if (tempPet.getId().equals(realPet.getId())) {
                tempPet = realPet;
            }
        }
        user.ifPresent(u -> u.setPets(pets));
        user.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Schedule added to pets need"));
    }

    @PutMapping("/updatePetSchedule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> updatePetSchedule(@Valid @RequestBody NeedRequest needRequest,
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
                need.setSchedules(needRequest.getSchedule());
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

    @GetMapping("/getReceivedPetById")
    @PreAuthorize("hasRole('USER')")
    public Object getAcceptedPetById(@RequestParam Map<String, String> id) {
        String receivedPetId = id.get("petId");
        String userId = id.get("userId");
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<ReceivedPet> receivedPets = user.getReceivedPets();
        List<ReceivedPet> filterPets = new ArrayList<>();
        for (ReceivedPet pet : receivedPets) {
            if (pet.getPetId().equals(receivedPetId)) {
                filterPets.add(pet);
            }
        }
        Optional<ReceivedPet> opPet = filterPets.stream().findFirst();
        ReceivedPet receivedPet = opPet.get();
        String senderId = receivedPet.getUserId();
        String petId = receivedPet.getPetId();
        Optional<User> opSender = userRepository.findById(senderId);
        User realSender = opSender.get();
        List<Pet> senderPets = realSender.getPets();
        List<Pet> filterSenderPets = new ArrayList<>();
        for (Pet senderPet : senderPets) {
            if (senderPet.getId().equals(petId)) {
                filterSenderPets.add(senderPet);
            }
        }
        Optional<Pet> correctPet = filterSenderPets.stream().findFirst();

        return correctPet;
    }

    @GetMapping("/getReceivedPets")
    @PreAuthorize("hasRole('USER')")
    public Object getReceivedPets(@RequestParam String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<ReceivedPet> receivedPets = user.getReceivedPets();
        List<ReceivedPet> filterReceivedPets = new ArrayList<>();
        List<InviteObject> receivedPetObjects = new ArrayList<>();
        for (ReceivedPet receivedPet : receivedPets) {
            String senderId = receivedPet.getUserId();
            Optional<User> opSender = userRepository.findById(senderId);
            User realSender = opSender.get();
            List<Pet> senderPets = realSender.getPets();
            InviteObject receivedPetObject;
            for (Pet pet : senderPets) {
                if (pet.getId().equals(receivedPet.getPetId())) {
                    receivedPetObject = new InviteObject(receivedPet.getId(), realSender, pet);
                    receivedPetObjects.add(receivedPetObject);
                    filterReceivedPets.add(receivedPet);
                }
            }

        }
        opUser.ifPresent(u -> u.setReceivedPets(filterReceivedPets));
        opUser.ifPresent(u -> userRepository.save(u));
        return receivedPetObjects;
    }

    @DeleteMapping("/deleteReceivedPetFromUser")
    @PreAuthorize("hasRole('USER')")
    public MessageObject deleteReceivedPetFromUser(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> opUser = userRepository.findById(userId);
        User realUser = opUser.get();
        List<ReceivedPet> pets = realUser.getReceivedPets();
        List<ReceivedPet> filterPets = pets.stream().filter(p -> !p.getPetId().equals(petId))
                .collect(Collectors.toList());
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

        opUser.ifPresent(u -> u.setReceivedPets(filterPets));
        opUser.ifPresent(u -> userRepository.save(u));
        String message = "pet has been deleted successfully";
        MessageObject object = new MessageObject(petId, message);
        return object;
    }

    @GetMapping("getAllInvites")
    @PreAuthorize("hasRole('USER')")
    public Object getAllInvites(@RequestParam String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<Invite> invites = user.getInvites();
        List<InviteObject> objectList = new ArrayList<>();
        List<Invite> filterInvites = new ArrayList<>();
        for (Invite invite : invites) {
            String senderId = invite.getUserId();
            Optional<User> opSender = userRepository.findById(senderId);
            User realSender = opSender.get();
            List<Pet> senderPets = realSender.getPets();
            InviteObject inviteObject = null;
            for (Pet pet : senderPets) {
                if (pet.getId().equals(invite.getPetId())) {
                    inviteObject = new InviteObject(invite.getId(), realSender, pet);
                    objectList.add(inviteObject);
                    filterInvites.add(invite);
                }
            }
        }
        opUser.ifPresent(u -> u.setInvites(filterInvites));
        opUser.ifPresent(u -> userRepository.save(u));

        return objectList;
    }

    @GetMapping("/getSharedWithUsers")
    @PreAuthorize("hasRole('USER')")
    public Object getSharedWithUsers(@RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<Pet> pets = user.getPets();
        List<Pet> filterPets = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getId().equals(petId)) {
                filterPets.add(pet);
            }
        }
        Optional<Pet> opPet = filterPets.stream().findFirst();
        Pet realPet = opPet.get();
        List<String> users = realPet.getSharedWith();
        List<User> realUsers = new ArrayList<>();
        for (String sender : users) {
            Optional<User> tempUser = userRepository.findById(sender);
            User realUser = tempUser.get();
            realUsers.add(realUser);
        }
        return realUsers;
    }

    @DeleteMapping("/removeReceivedUser")
    @PreAuthorize("hasRole('USER')")
    public MessageObject removeReceivedUser(@RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        String receiverId = id.get("receiverId");
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<Pet> pets = user.getPets();
        List<Pet> filterPets = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getId().equals(petId)) {
                filterPets.add(pet);
            }
        }
        Optional<Pet> opPet = filterPets.stream().findFirst();
        Pet realPet = opPet.get();
        List<String> users = realPet.getSharedWith();
        List<String> filterUsers = new ArrayList<>();
        for (String receiver : users) {
            Optional<User> tempUser = userRepository.findById(receiver);
            User realUser = tempUser.get();
            if(!realUser.getId().equals(receiverId)) {
                filterUsers.add(realUser.getId());
            }
        }
        realPet.setSharedWith(filterUsers); 
        for(Pet pet: pets) {
            if(pet.getId().equals(realPet.getId())) {
                pet.equals(realPet); 
            }
        }
        opUser.ifPresent(u -> u.setPets(pets));
        opUser.ifPresent(u -> userRepository.save(u)); 
        String message = "user has been deleted successfully";

        MessageObject object = new MessageObject(receiverId, message);
        return object;
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
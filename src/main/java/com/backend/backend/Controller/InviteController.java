package com.backend.backend.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.backend.backend.Model.Invite;
import com.backend.backend.Model.MessageObject;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.ReceivedPet;
import com.backend.backend.Model.User;
import com.backend.backend.Payload.request.InviteRequest;
import com.backend.backend.Payload.response.MessageResponse;
import com.backend.backend.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class InviteController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/sendInvite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> sendInvite(@Valid @RequestBody InviteRequest inviteRequest,
            @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        Optional<User> receiver = userRepository.findByUsername(inviteRequest.getUsername());
        if (!receiver.isPresent()) {
            return ResponseEntity.ok(new MessageResponse("User does not exist"));
        }
        User realReceiver = receiver.get();
        Invite invite = new Invite(userId, petId);

        List<Invite> receiverInvites = realReceiver.getInvites();
        List<ReceivedPet> receiverReceivedPets = realReceiver.getReceivedPets();
        for (Invite tempInvite : receiverInvites) {
            if (tempInvite.getPetId().equals(invite.getPetId())) {
                return ResponseEntity
                        .ok(new MessageResponse("Invite for this pet has already been sent to this user!"));
            }
        }

        for (ReceivedPet receivedPet : receiverReceivedPets) {
            if (receivedPet.getPetId().equals(invite.getPetId())) {
                return ResponseEntity.ok(new MessageResponse("This pet has already been received by that user!"));
            }
        }

        receiver.ifPresent(u -> u.addInvite(invite));
        receiver.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Invite has been sent!"));
    }

    @PostMapping("/acceptInvite")
    @PreAuthorize("hasRole('USER')")
    public MessageObject acceptInvite(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String inviteId = id.get("inviteId");

        Optional<User> receiver = userRepository.findById(userId);
        List<Invite> invites = receiver.get().invites;
        List<Invite> filterInvites = invites.stream().filter(i -> i.getId().equals(inviteId))
                .collect(Collectors.toList());
        Optional<Invite> setInvite = filterInvites.stream().findFirst();

        String receivedPetId = setInvite.get().getPetId();

        String senderId = setInvite.get().getUserId();
        Optional<User> opSender = userRepository.findById(senderId);
        ReceivedPet acceptedPet = new ReceivedPet(receivedPetId, senderId);
        User sender = opSender.get();
        List<Pet> senderPets = sender.getPets();

        List<Pet> updatePets = new ArrayList<>();
        for (Pet pet : senderPets) {
            if (pet.getId().equals(receivedPetId)) {
                pet.addSharedWith(userId);
                updatePets.add(pet);
            } else {
                updatePets.add(pet);

            }
        }

        opSender.ifPresent(u -> u.setPets(updatePets));
        opSender.ifPresent(u -> userRepository.save(u));

        receiver.ifPresent(u -> u.receivePet(acceptedPet));
        List<Invite> removeInvite = invites.stream().filter(i -> !i.getId().equals(inviteId))
                .collect(Collectors.toList());
        receiver.ifPresent(u -> u.setInvites(removeInvite));
        receiver.ifPresent(u -> userRepository.save(u));
        String message = "invite has been added successfully";

        MessageObject object = new MessageObject(inviteId, message);
        return object;
    }

    @DeleteMapping("/deleteInvite")
    @PreAuthorize("hasRole('USER')")
    public MessageObject deleteInvite(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String inviteId = id.get("inviteId");

        Optional<User> receiver = userRepository.findById(userId);
        List<Invite> invites = receiver.get().invites;
        List<Invite> removeInvite = invites.stream().filter(i -> !i.getId().equals(inviteId))
                .collect(Collectors.toList());
        receiver.ifPresent(u -> u.setInvites(removeInvite));
        receiver.ifPresent(u -> userRepository.save(u));
        String message = "invite has been deleted successfully";

        MessageObject object = new MessageObject(inviteId, message);
        return object; 
    }
}

package com.backend.backend.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.backend.backend.Model.Need;
import com.backend.backend.Model.Notification;
import com.backend.backend.Model.NotificationObject;
import com.backend.backend.Model.Pet;
import com.backend.backend.Model.Schedule;
import com.backend.backend.Model.User;
import com.backend.backend.Payload.response.MessageResponse;
import com.backend.backend.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createNotification")
    public ResponseEntity<MessageResponse> createNotification(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String petId = id.get("petId");
        String needId = id.get("needId");
        String scheduleId = id.get("scheduleId");
        Notification notification = new Notification(petId, needId, scheduleId);
        Optional<User> opUser = userRepository.findById(userId);
        opUser.ifPresent(u -> u.addNotification(notification));
        opUser.ifPresent(u -> userRepository.save(u));
        return ResponseEntity.ok(new MessageResponse("Notification has been made"));

    }

    @GetMapping("/getPetNotifications")
    public List<NotificationObject> getPetNotifications(@Valid @RequestParam Map<String, String> id) {
        List<NotificationObject> notificationObjects = new ArrayList<>();

        String userId = id.get("userId");
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<Notification> notifications = user.getNotifications();
        for (Notification notification : notifications) {
            String notificationId = notification.getId();
            String petId = notification.getPetId();
            String needId = notification.getNeedId();
            String scheduleId = notification.getScheduleId();
            List<Pet> pets = user.getPets();
            List<Pet> filterPets = new ArrayList<>();
            NotificationObject notificationObject;
            List<Need> filterNeeds = new ArrayList<>();
            List<Schedule> filterSchedule = new ArrayList<>();
            for (Pet pet : pets) {
                if (pet.getId().equals(petId)) {
                    filterPets.add(pet);
                }
                Optional<Pet> opPet = filterPets.stream().findFirst();
                Pet realPet = opPet.get();
                List<Need> petNeeds = realPet.getNeeds();

                for (Need petNeed : petNeeds) {
                    if (petNeed.getId().equals(needId)) {
                        filterNeeds.add(petNeed);
                        List<Schedule> schedules = petNeed.getSchedules();
                        for (Schedule petSchedule : schedules) {
                            if (petSchedule.getId().equals(scheduleId)) {
                                filterSchedule.add(petSchedule);
                            }
                        }
                    }
                }

            }
            Optional<Pet> opPet = filterPets.stream().findFirst();
            Pet realPet = opPet.get();
            Optional<Need> opNeed = filterNeeds.stream().findFirst();
            Need realNeed = opNeed.get();
            Optional<Schedule> opSchedule = filterSchedule.stream().findFirst();
            Schedule realSchedule = opSchedule.get();
            notificationObject = new NotificationObject(notificationId, realPet, realNeed, realSchedule);
            notificationObjects.add(notificationObject);
        }
        return notificationObjects;
    }
}

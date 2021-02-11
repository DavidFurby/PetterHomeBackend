package com.backend.backend.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.backend.backend.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    @Scheduled(fixedRate = 60000)
    public void createNotification() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String userId = user.getId();
            List<Pet> pets = user.getPets();
            if (pets != null) {
                for (Pet pet : pets) {
                    String petId = pet.getId();
                    List<Need> needs = pet.getNeeds();
                    if (needs != null) {
                        for (Need need : needs) {
                            String needId = need.getId();
                            List<Schedule> schedules = need.getSchedules();
                            if (schedules != null) {
                                for (Schedule schedule : schedules) {
                                    String scheduleId = schedule.getId();
                                    String assignedUsername = schedule.getAssignedUser();
                                    Optional<User> opAssignedUser = userRepository.findByUsername(assignedUsername);
                                    User assignedUser = opAssignedUser.get();
                                    String assignedUserId = assignedUser.getId();
                                    Optional<User> test = userRepository.findById(assignedUserId);
                                    String time = "21:56";
                                    if (time.equals(dateFormat.format(new Date()))) {
                                        Notification notification = new Notification(petId, needId, scheduleId, userId,
                                                assignedUserId, false);
                                        Optional<User> opUser = userRepository.findById(userId);
                                        System.out.println(!userId.equals(assignedUserId));
                                        if (!userId.equals(assignedUserId)) {
                                            test.ifPresent(u -> u.addNotification(notification));
                                            test.ifPresent(u -> userRepository.save(u));
                                        }
                                        opUser.ifPresent(u -> u.addNotification(notification));
                                        opUser.ifPresent(u -> userRepository.save(u));

                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @GetMapping("/getPetNotifications")
    @PreAuthorize("hasRole('USER')")
    public List<NotificationObject> getPetNotifications(@Valid @RequestParam Map<String, String> id) {
        List<NotificationObject> notificationObjects = new ArrayList<>();

        String userId = id.get("userId");
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<Notification> notifications = user.getNotifications();
        List<Notification> filterNotification = new ArrayList<Notification>();
        for (Notification notification : notifications) {
            String notificationId = notification.getId();
            String petId = notification.getPetId();
            String needId = notification.getNeedId();
            String scheduleId = notification.getScheduleId();
            Boolean check = notification.getChecked();
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
                if (opPet.isPresent()) {
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

            }
            Optional<Pet> opPet = filterPets.stream().findFirst();
            Optional<Schedule> opSchedule = filterSchedule.stream().findFirst();
            Optional<Need> opNeed = filterNeeds.stream().findFirst();
            if (opPet.isPresent()) {
                Pet realPet = opPet.get();
                Need realNeed = opNeed.get();
                Schedule realSchedule = opSchedule.get();
                notificationObject = new NotificationObject(notificationId, realPet, realNeed, realSchedule, check);
                notificationObjects.add(notificationObject);
                filterNotification.add(notification);
            }
        }
        opUser.ifPresent(u -> u.setNotifications(filterNotification));
        opUser.ifPresent(u -> userRepository.save(u));
        return notificationObjects;
    }

    @PutMapping("/checkNotification")
    @PreAuthorize("hasRole('USER')")
    public void checkNotification(@Valid @RequestParam Map<String, String> id) {
        String userId = id.get("userId");
        String notificationId = id.get("notificationId");
        Optional<User> opUser = userRepository.findById(userId);
        User user = opUser.get();
        List<Notification> notifications = user.getNotifications();
        List<Notification> newNotification = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notification.setChecked(true);
                newNotification.add(notification);
            } else {
                newNotification.add(notification);

            }
        }
        opUser.ifPresent(u -> u.setNotifications(newNotification));
        opUser.ifPresent(u -> userRepository.save(u));
    }
}

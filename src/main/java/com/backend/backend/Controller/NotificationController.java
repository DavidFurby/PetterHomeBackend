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
import com.backend.backend.Repository.NotificationRepository;
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
    @Autowired
    private NotificationRepository notificationRepository;

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
                                    String time = schedule.getTime(); 
                                    Optional<User> opUser = userRepository.findById(userId);
                                    if (time.equals(dateFormat.format(new Date()))) {
                                        Notification notification = new Notification(petId, needId, scheduleId, userId,
                                                assignedUserId, false);
                                        String notificationId = notification.getId();
                                        notificationRepository.save(notification);
                                        opUser.ifPresent(u -> u.addNotification(notificationId));
                                        opUser.ifPresent(u -> userRepository.save(u));
                                        List<String> sharedWithUsers = pet.getSharedWith();
                                        for(String sharedWithUser: sharedWithUsers) {
                                            Optional<User> opSharedWithUser = userRepository.findById(sharedWithUser);
                                            opSharedWithUser.ifPresent(u -> u.addNotification(notificationId));
                                        }
                                        if (!userId.equals(assignedUserId)) {
                                            opAssignedUser.ifPresent(u -> u.addNotification(notificationId));
                                            opAssignedUser.ifPresent(u -> userRepository.save(u));
                                        }
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
        List<String> notifications = user.getNotifications();
        for (String notification : notifications) {
            Optional<Notification> opNotification = notificationRepository.findById(notification);
            Notification realNotification = opNotification.get();
            Boolean checked = realNotification.getChecked();
            String petId = realNotification.getPetId();
            String needId = realNotification.getNeedId();
            String scheduleId = realNotification.getScheduleId();
            List<User> users = userRepository.findAll();
            for (User listUser : users) {
                List<Pet> pets = listUser.getPets();
                for (Pet pet : pets) {
                    if (pet.getId().equals(petId)) {
                        List<Need> needs = pet.getNeeds();
                        for (Need need : needs) {
                            if (need.getId().equals(needId)) {
                                List<Schedule> schedules = need.getSchedules();
                                for (Schedule schedule : schedules) {
                                    if (schedule.getId().equals(scheduleId)) {
                                        NotificationObject notificationObject = new NotificationObject(notification,
                                                pet, need, schedule, checked);
                                        notificationObjects.add(notificationObject);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return notificationObjects;
    }

    @PutMapping("/checkNotification")
    @PreAuthorize("hasRole('USER')")
    public void checkNotification(@Valid @RequestParam Map<String, String> id) {
        String notificationId = id.get("notificationId");

        Optional<Notification> opNotification = notificationRepository.findById(notificationId);
        Notification realNotification = opNotification.get();
        Boolean check = realNotification.getChecked();
        opNotification.ifPresent(n -> n.setChecked(!check));
        opNotification.ifPresent(n -> notificationRepository.save(n));
    }
}

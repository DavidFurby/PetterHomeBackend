package com.backend.backend.Model;

import javax.validation.constraints.NotBlank;

public class NotificationObject {

    @NotBlank
    public String notificationId;
    @NotBlank
    public Pet pet;
    @NotBlank
    public Need need;
    @NotBlank
    public Schedule schedule;
    @NotBlank
    public Boolean check;

    public NotificationObject(@NotBlank String notificationId, Pet pet, Need need, Schedule schedule, Boolean check) {
        this.notificationId = notificationId;
        this.pet = pet;
        this.need = need;
        this.schedule = schedule;
        this.check = check;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public Pet getPet() {
        return pet;
    }

    public Need getNeed() {
        return need;
    }

    public Schedule getSchedule() {
        return schedule;
    }
    public Boolean getCheck() {
        return check; 
    }
}

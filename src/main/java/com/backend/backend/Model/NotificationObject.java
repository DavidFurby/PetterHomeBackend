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

    public NotificationObject(@NotBlank String notificationId, Pet pet, Need need,
            Schedule schedule) {
        this.notificationId = notificationId;
        this.pet = pet;
        this.need = need;
        this.schedule = schedule;
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
}

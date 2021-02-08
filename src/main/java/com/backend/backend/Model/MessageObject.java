package com.backend.backend.Model;

import javax.validation.constraints.NotBlank;


public class MessageObject {

    public String objectId;
    @NotBlank
    public String msg;


    public MessageObject(@NotBlank String userId, String msg) {
        this.objectId = userId;
        this.msg = msg;
    }

}

package com.example.waitingroom.config;

import com.example.waitingroom.domain.ParticipantsInfo;
import lombok.Getter;

@Getter
public class UserJoinedMessage {
    private String type;
    private ParticipantsInfo participantInfo;

    public UserJoinedMessage(ParticipantsInfo participantInfo) {
        this.type = "USER_JOINED";
        this.participantInfo = participantInfo;
    }

}

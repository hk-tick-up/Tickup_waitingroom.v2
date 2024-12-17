package com.example.waitingroom.DTO;

import com.example.waitingroom.domain.ParticipantsInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponseDto {
    private String userId;
    private String nickname;
    private String gameType;

    public static ParticipantResponseDto from(ParticipantsInfo participant) {
        return new ParticipantResponseDto(
                participant.getUserId(),
                participant.getNickname(),
                participant.getGameType()
        );
    }
}

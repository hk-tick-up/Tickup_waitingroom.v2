package com.example.waitingroom.request;

import com.example.waitingroom.domain.WaitingRooms;

import java.util.UUID;

public record CreateWaitingRoomRequest(
        WaitingRooms.GameType GameType,
        String userRole
) {
    public WaitingRooms toEntityPublic(){
        return WaitingRooms.builder()
                .gameRoomCode("")
                .gameType(WaitingRooms.GameType.Basic)
                .participants(1)
                .maxParticipants(5)
                .isPublic(true)
                .isStarted(false)
                .build();
    }
    public WaitingRooms toEntityPrivate(){
        UUID uuid = UUID.randomUUID();
        String randomCode = uuid.toString().substring(0,5).toUpperCase();
        return WaitingRooms.builder()
                .gameRoomCode(randomCode)
                .gameType(WaitingRooms.GameType.Basic)
                .maxParticipants(5)
                .participants(1)
                .isPublic(false)
                .isStarted(false)
                .build();
    }
    public WaitingRooms toEntityContest(){
        return WaitingRooms.builder()
                .gameRoomCode("")
                .gameType(WaitingRooms.GameType.Contest)
                .maxParticipants(null)
                .isPublic(true)
                .isStarted(false)
                .build();
    }
}

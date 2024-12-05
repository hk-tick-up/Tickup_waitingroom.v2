package com.example.waitingroom.service;


import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WaitingRoomsService {
    WaitingRooms createWaitingGameRoom(CreateWaitingRoomRequest CWReq, HttpServletRequest httpRequest);
    WaitingRooms joinPrivateWaitingRoom(String gameRoomCode, HttpServletRequest httpRequest);
    WaitingRooms createOrJoinPublicRoom(CreateWaitingRoomRequest req);
    WaitingRooms createContestWaitingRoom(CreateWaitingRoomRequest req);
    WaitingRooms joinContestWaitingRoom(Long gameRoomId);
    void updateRoomParticipants(Long gameRoomId, int participants);
    void addParticipantToRoom(Long gameRoomId, HttpServletRequest httpRequest);
    void validateUserSession(HttpServletRequest httpRequest);
    List<ParticipantsInfo> getParticipantsFromRedis(Long gameRoomId);
    void removeParticipantsFromRoom(Long roomId, String userId);
    void decrementParticipants(Long gameRoomId);
}


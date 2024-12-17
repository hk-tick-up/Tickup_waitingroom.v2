package com.example.waitingroom.service;


import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface WaitingRoomsService {
    WaitingRooms createWaitingGameRoom(CreateWaitingRoomRequest CWReq, HttpServletRequest httpRequest);
    WaitingRooms findRoomIdByCode(String gameRoomCode);
    WaitingRooms joinPrivateWaitingRoom(String gameRoomCode, HttpServletRequest httpRequest);
    WaitingRooms createOrJoinPublicRoom(CreateWaitingRoomRequest req);
    WaitingRooms createContestWaitingRoom(CreateWaitingRoomRequest req);
    WaitingRooms joinContestWaitingRoom(Long waitingRoomId);
    void incrementParticipants(Long waitingRoomId);
    void validateUserSession(HttpServletRequest httpRequest);
    WaitingRooms decrementParticipants(Long waitingRoomId);

}


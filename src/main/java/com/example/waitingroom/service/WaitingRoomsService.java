package com.example.waitingroom.service;


import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.repository.WaitingRoomRepository;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface WaitingRoomsService {
    WaitingRooms findWaitingRoomsById(Long id);
    WaitingRooms createPrivateGameRoom(CreateWaitingRoomRequest req);
    WaitingRooms joinPrivateGameRoom(String gameRoomCode);
    WaitingRooms createOrJoinPublicRoom(CreateWaitingRoomRequest req);
    WaitingRooms createContestGameRoom(CreateWaitingRoomRequest req);
}


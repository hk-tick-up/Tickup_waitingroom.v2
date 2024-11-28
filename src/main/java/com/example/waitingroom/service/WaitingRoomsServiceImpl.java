package com.example.waitingroom.service;

import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.repository.WaitingRoomRepository;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WaitingRoomsServiceImpl extends WaitingRooms {
    private final WaitingRoomRepository waitingRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    //private room 만드는 API
    public WaitingRooms createWaitingGameRoom(CreateWaitingRoomRequest req) {
        if(req.GameType() == GameType.Contest && !req.userRole().equals("Admin")) {
            throw new IllegalArgumentException("Contest 게임 방은 관리자만 생성할 수 있습니다.");
        }

        String gameType = req.GameType().name();
        if(!GameType.checking(gameType)) {
            throw new IllegalArgumentException("지원하지 않는 게임 형식입니다.");
        }

        WaitingRooms newWaitingRoom = req.toEntityPrivate();
        return waitingRoomRepository.save(newWaitingRoom);
    }

    //private room 참여하는 API
    public WaitingRooms joinPrivateWaitingRoom(String gameRoomCode) {
        WaitingRooms waitingRoom = waitingRoomRepository.findByGameRoomCode(gameRoomCode);
        if(waitingRoom == null) {
            throw new IllegalArgumentException("존재하지 않는 게임방입니다.");
        }

        if(waitingRoom.isPublic()) {
            throw new IllegalArgumentException("해당 게임방에는 입장할 수 없습니다.");
        }

        if(waitingRoom.isStarted()) {
           throw new IllegalArgumentException("이미 게임이 시작한 방입니다.");
        }

        if(!waitingRoom.incrementParticipants()) {
            throw new IllegalArgumentException("방이 가득 찼습니다.");
        }
        return waitingRoomRepository.save(waitingRoom);
    }

    //랜덤 공개방을 만들거나 참여하는 API
    public WaitingRooms createOrJoinPublicRoom(CreateWaitingRoomRequest req) {
        if(req.GameType() == GameType.Contest && !req.userRole().equals("Admin")) {
            throw new IllegalArgumentException("Contest 게임 방은 관리자만 생성할 수 있습니다.");
        }

        if(!GameType.checking(String.valueOf(req.GameType()))) {
            throw new IllegalArgumentException("지원하지 않는 게임 형식입니다.");
        }

        WaitingRooms availbleRoom = waitingRoomRepository.findExistWaitingRooms();
        if(availbleRoom != null) {
            if(!availbleRoom.incrementParticipants()) {
                throw new IllegalArgumentException("방이 가득 찼습니다");
            }
            messagingTemplate.convertAndSend(
                    "/topic/public/"+availbleRoom.getId(),
                    "User joined the room. Current participants: " + availbleRoom.getParticipants()
            );
            return waitingRoomRepository.save(availbleRoom);
        }

        WaitingRooms newGameRoom = req.toEntityPublic();
        WaitingRooms savedRoom = waitingRoomRepository.save(newGameRoom);

        messagingTemplate.convertAndSend(
                "/topic/public/new",
                "New pulbic game room created. Room Id: " + savedRoom.getId()
        );

        return savedRoom;
    }

    //Contest 방 만드는 API
    public WaitingRooms createContestWaitingRoom(CreateWaitingRoomRequest req) {
        if(req.GameType() == GameType.Contest && !req.userRole().equals("Admin")) {
            throw new IllegalArgumentException("Contest 게임 방은 관리자만 생성할 수 있습니다.");
        }
        WaitingRooms newGameRoom = req.toEntityContest();
        return waitingRoomRepository.save(newGameRoom);
    }

    //Contest 방 참여하는 API
    public WaitingRooms joinContestWaitingRoom(Long gameRoomId) {
        WaitingRooms waitingRoom = waitingRoomRepository.findWaitingRoomsById(gameRoomId);
        if(waitingRoom == null ){
            throw new IllegalArgumentException("존재하지 않는 게임방입니다.");
        }

        return waitingRoomRepository.save(waitingRoom);
    }

}

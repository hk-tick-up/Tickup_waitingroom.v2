package com.example.waitingroom.controller;

import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.repository.ParticipantRepository;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import com.example.waitingroom.service.WaitingRoomsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/waiting-room")
@RequiredArgsConstructor
public class WaitingRoomController {
    private final WaitingRoomsServiceImpl waitingRoomsService;
//    private final RedisParticipantsRepository redisParticipantsRepository;
    private final ParticipantRepository participantRepository;

    @PostMapping("/create-private")
    public ResponseEntity<?> createPrivateWaitingRoom(@RequestBody CreateWaitingRoomRequest CWReq, HttpServletRequest HSReq) {
        WaitingRooms newWaitingRoom = waitingRoomsService.createWaitingGameRoom(CWReq, HSReq);
        return ResponseEntity.ok(newWaitingRoom);
    }

    // -->수정요망.
    @PostMapping("/join/{gameRoomCode}")
    public ResponseEntity<?> joinPrivateWaitingRoom(@PathVariable String gameRoomCode) {
        WaitingRooms waitingRoom = waitingRoomsService.findRoomIdByCode(gameRoomCode);
        waitingRoomsService.incrementParticipants(waitingRoom.getId());
        return getResponseEntity(waitingRoom);
    }

    @PutMapping("/leave/{gameRoomId}")
    public ResponseEntity<?> leaveTheRoom(@PathVariable Long gameRoomId) {
        waitingRoomsService.decrementParticipants(gameRoomId);
        return ResponseEntity.ok("방을 성공적으로 나갔습니다.");
    }

    @PostMapping("/random-join")
    public ResponseEntity<?> createOrJoinPublicRoom(@RequestBody CreateWaitingRoomRequest request) {
        WaitingRooms room = waitingRoomsService.createOrJoinPublicRoom(request);
        return getResponseEntity(room);
    }

    private ResponseEntity<?> getResponseEntity(WaitingRooms room) {
        if (room != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("roomId", room.getId());
            response.put("gameRoomCode", room.getGameRoomCode());
            response.put("gameType", room.getGameType());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-contest")
    public ResponseEntity<?> createContestWaitingRoom(@RequestBody CreateWaitingRoomRequest req) {
        WaitingRooms newWaitingRoom = waitingRoomsService.createContestWaitingRoom(req);
        return ResponseEntity.ok(newWaitingRoom);
    }

    @PostMapping("/join/contest")
    public ResponseEntity<?> joinContestWaitingRoom(@PathVariable String gameRoomId) {
        WaitingRooms waitingRoom = waitingRoomsService.joinContestWaitingRoom(Long.valueOf(gameRoomId));
        return getResponseEntity(waitingRoom);
    }



//    @GetMapping("/participants/{waitingRoomId}")
//    public ResponseEntity<List<ParticipantsInfo>> getParticipants(@PathVariable Long waitingRoomId) {
//        List<ParticipantsInfo> participants = redisParticipantsRepository.getParticipants(waitingRoomId);
//        return ResponseEntity.ok(participants);
//    }
//
//    @DeleteMapping("/participants/{waitingRoomId}")
//    public ResponseEntity<?> removeParticipants(@PathVariable Long waitingRoomId) {
//        redisParticipantsRepository.deleteParticipants(waitingRoomId);
//        return ResponseEntity.ok("Participants removed for waitingRoomId: " + waitingRoomId);
//    }
}

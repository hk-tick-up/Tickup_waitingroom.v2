package com.example.waitingroom.controller;

import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.repository.RedisParticipantsRepository;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import com.example.waitingroom.service.WaitingRoomsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    private final RedisParticipantsRepository redisParticipantsRepository;

    @PostMapping("/create-private")
    public ResponseEntity<?> createPrivateWaitingRoom(@RequestBody CreateWaitingRoomRequest CWReq, HttpServletRequest HSReq) {
        WaitingRooms newWaitingRoom = waitingRoomsService.createWaitingGameRoom(CWReq, HSReq);
        return ResponseEntity.ok(newWaitingRoom);
    }

    @PostMapping("/create-contest")
    public ResponseEntity<?> createContestWaitingRoom(@RequestBody CreateWaitingRoomRequest req) {
        WaitingRooms newWaitingRoom = waitingRoomsService.createContestWaitingRoom(req);
        return ResponseEntity.ok(newWaitingRoom);
    }

    @PostMapping("/join/{gameRoomCode}")
    public ResponseEntity<?> joinPrivateWaitingRoom(@PathVariable String gameRoomCode) {
        WaitingRooms waitingRoom = waitingRoomsService.findRoomIdByCode(gameRoomCode);
        return getResponseEntity(waitingRoom);
    }

    @PostMapping("/join/contest")
    public ResponseEntity<?> joinContestWaitingRoom(@PathVariable String gameRoomId) {
        WaitingRooms waitingRoom = waitingRoomsService.joinContestWaitingRoom(Long.valueOf(gameRoomId));
        return getResponseEntity(waitingRoom);
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

    @GetMapping("/participants/{waitingRoomId}")
    public ResponseEntity<List<ParticipantsInfo>> getParticipants(@PathVariable Long waitingRoomId) {
        List<ParticipantsInfo> participants = redisParticipantsRepository.getParticipants(waitingRoomId);
        return ResponseEntity.ok(participants);
    }

    @DeleteMapping("/participants/{waitingRoomId}")
    public ResponseEntity<?> removeParticipants(@PathVariable Long waitingRoomId) {
        redisParticipantsRepository.deleteParticipants(waitingRoomId);
        return ResponseEntity.ok("Participants removed for waitingRoomId: " + waitingRoomId);
    }
}

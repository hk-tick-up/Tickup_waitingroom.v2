package com.example.waitingroom.controller;

import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import com.example.waitingroom.service.WaitingRoomsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/waiting-room")
@RequiredArgsConstructor
public class WaitingRoomController {
    private final WaitingRoomsServiceImpl waitingRoomsService;

    @PostMapping("/create-private")
    public ResponseEntity<?> createPrivateWaitingRoom(@RequestBody CreateWaitingRoomRequest req) {
        WaitingRooms newWaitingRoom = waitingRoomsService.createWaitingGameRoom(req);
        return ResponseEntity.ok(newWaitingRoom);
    }

    @PostMapping("/create-contest")
    public ResponseEntity<?> createContestWaitingRoom(@RequestBody CreateWaitingRoomRequest req) {
        WaitingRooms newWaitingRoom = waitingRoomsService.createContestWaitingRoom(req);
        return ResponseEntity.ok(newWaitingRoom);
    }

    @PostMapping("/join/{gameRoomCode}")
    public ResponseEntity<?> joinPrivateWaitingRoom(@PathVariable String gameRoomCode) {
        WaitingRooms waitingRoom = waitingRoomsService.joinPrivateWaitingRoom(gameRoomCode);
        return ResponseEntity.ok(waitingRoom);
    }

    @PostMapping("/join/contest")
    public ResponseEntity<?> joinContestWaitingRoom(@PathVariable String gameRoomId) {
        WaitingRooms waitingRoom = waitingRoomsService.joinContestWaitingRoom(Long.valueOf(gameRoomId));
        return ResponseEntity.ok(waitingRoom);
    }

    @PostMapping("/random-join")
    public ResponseEntity<String> createOrJoinPublicRoom(@RequestBody CreateWaitingRoomRequest req) {
        WaitingRooms room = waitingRoomsService.createOrJoinPublicRoom(req);
        return ResponseEntity.ok("WebSocket 연결 경로 : /topic" + room.getId());
    }
}

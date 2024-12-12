package com.example.waitingroom.config.websocket;

import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.domain.User;
import com.example.waitingroom.repository.RedisParticipantsRepository;
import com.example.waitingroom.repository.WaitingRoomRepository;
import com.example.waitingroom.service.WaitingRoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RedisParticipantsRepository redisParticipantsRepository;
    private final WaitingRoomsService waitingRoomsService;
    private final WaitingRoomRepository waitingRoomRepository;
    private final Map<Long, List<User>> roomUsers = new ConcurrentHashMap<>();

    @MessageMapping("/waiting-room/{gameRoomId}")
    @SendTo("/topic/waiting-room/{gameRoomId}")
    public List<User> addParticipant(
            @DestinationVariable Long gameRoomId,
            User user  // HttpServletRequest 제거
    ) {
        List<User> users = roomUsers.getOrDefault(gameRoomId, new ArrayList<>());
        if (users.stream().noneMatch(u -> u.getId().equals(user.getId()))) {
            users.add(user);
            roomUsers.put(gameRoomId, users);
        }
        return users;
    }


    @MessageMapping("/waiting-room/{gameRoomId}/leave")
    @SendTo("/topic/waiting-room/{gameRoomId}/leave")
    public List<ParticipantsInfo> handleLeaveRoom(
            @DestinationVariable Long gameRoomId,
            @Payload Map<String, String> payload
    ) {
       try {
           String userId = payload.get("userId");
           waitingRoomsService.decrementParticipants(gameRoomId);

           // Redis에서 참가자 제거
//           waitingRoomsService.removeParticipantsFromRoom(gameRoomId, userId);

           // 업데이트된 참가자 목록 반환
           return redisParticipantsRepository.getParticipants(gameRoomId);
       } catch (Exception e) {
           throw e;
       }
    }

    @MessageMapping("/waiting-room/{gameRoomId}/status")
    @SendTo("/topic/waiting-room/{gameRoomId}/status")
    public List<ParticipantsInfo> handleStatusUpdate(
            @DestinationVariable Long gameRoomId,
            @Payload ParticipantsInfo participant
    ) {
        List<ParticipantsInfo> participants = redisParticipantsRepository.getParticipants(gameRoomId);

        // 상태 업데이트 로직
        participants.stream()
                .filter(p -> p.getUserId().equals(participant.getUserId()))
                .findFirst()
                .ifPresent(p -> {
                    // 상태 업데이트 로직 추가 필요
                    // p.setStatus(participant.getStatus());
                });

        // 업데이트된 참가자 목록 저장
        redisParticipantsRepository.saveParticipants(gameRoomId, participants);

        return participants;
    }

    @MessageMapping("/waiting-room/{gameRoomId}/start")
    @SendTo("/topic/waiting-room/{gameRoomId}")
    public String handleGameStart(@DestinationVariable Long gameRoomId) {
        // 게임 시작 처리 로직
        // waitingRoomsService.startGame(gameRoomId);
        return "GAME_START";
    }
}
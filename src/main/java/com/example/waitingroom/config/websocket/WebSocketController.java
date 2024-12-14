package com.example.waitingroom.config.websocket;

import com.example.waitingroom.config.UserJoinedMessage;
import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.domain.User;
import com.example.waitingroom.repository.WaitingRoomRepository;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import com.example.waitingroom.service.ParticipantService;
import com.example.waitingroom.service.WaitingRoomsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {
    private final ParticipantService participantService;
    private final WaitingRoomsService waitingRoomsService;
    private final WaitingRoomRepository waitingRoomRepository;

    private final Map<Long, List<User>> roomUsers = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
    }

    @MessageMapping("/waiting-room/{gameRoomId}")
    @SendTo("/topic/waiting-room/{gameRoomId}")
    public List<ParticipantsInfo> addParticipant(
            @DestinationVariable Long gameRoomId,
            @Payload ParticipantsInfo participants
    ) {
        participantService.addParticipant(gameRoomId, participants);

        return participantService.participantsList(gameRoomId);
    }

    @MessageMapping("/waiting-room/leave/{gameRoomId}")
    @SendTo("/topic/waiting-room/{gameRoomId}")
    public List<ParticipantsInfo> handleLeaveRoom(
            @DestinationVariable Long gameRoomId,
            @Payload ParticipantsInfo participants
    ) {
        String userId = participants.getUserId();

        participantService.removeParticipants(gameRoomId, userId);
        return participantService.participantsList(gameRoomId);
    }

//    @MessageMapping("/waiting-room/{gameRoomId}/status")
//    @SendTo("/topic/waiting-room/{gameRoomId}/status")
//    public List<ParticipantsInfo> handleStatusUpdate(
//            @DestinationVariable Long gameRoomId,
//            @Payload ParticipantsInfo participant
//    ) {
//        List<ParticipantsInfo> participants = redisParticipantsRepository.getParticipants(gameRoomId);
//
//        // 상태 업데이트 로직
//        participants.stream()
//                .filter(p -> p.getUserId().equals(participant.getUserId()))
//                .findFirst()
//                .ifPresent(p -> {
//                    // 상태 업데이트 로직 추가 필요
//                    // p.setStatus(participant.getStatus());
//                });
//
//        // 업데이트된 참가자 목록 저장
//        redisParticipantsRepository.saveParticipants(gameRoomId, participants);
//
//        return participants;
//    }

    @MessageMapping("/waiting-room/{gameRoomId}/start")
    @SendTo("/topic/waiting-room/{gameRoomId}")
    public String handleGameStart(@DestinationVariable Long gameRoomId) {
        // 게임 시작 처리 로직
        // waitingRoomsService.startGame(gameRoomId);
        return "GAME_START";
    }
}
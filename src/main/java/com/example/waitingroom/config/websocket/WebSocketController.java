package com.example.waitingroom.config.websocket;

import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.repository.RedisParticipantsRepository;
import com.example.waitingroom.service.WaitingRoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RedisParticipantsRepository redisParticipantsRepository;
    private final WaitingRoomsService waitingRoomsService;

    @MessageMapping("/waiting-room/{gameRoomId}")
    @SendTo("/topic/waiting-room/{gameRoomId}")
    public List<ParticipantsInfo> handleJoinRoom(
            @DestinationVariable Long gameRoomId,
            WebSocketSession session,
            ParticipantsInfo participant
    ) {
        System.out.println("connect");
        // Redis에 참가자 추가
        waitingRoomsService.addParticipantToRoom(gameRoomId, participant);

        // 업데이트된 참가자 목록 반환
        return redisParticipantsRepository.getParticipants(gameRoomId);
    }

    @MessageMapping("/waiting-room/{gameRoomId}/leave")
    @SendTo("/topic/waiting-room/{gameRoomId}")
    public List<ParticipantsInfo> handleLeaveRoom(
            @DestinationVariable Long gameRoomId,
            @Payload String userId
    ) {
        // Redis에서 참가자 제거
        waitingRoomsService.removeParticipantsFromRoom(gameRoomId, userId);

        // 업데이트된 참가자 목록 반환
        return redisParticipantsRepository.getParticipants(gameRoomId);
    }

    @MessageMapping("/waiting-room/{gameRoomId}/status")
    @SendTo("/topic/waiting-room/{gameRoomId}")
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
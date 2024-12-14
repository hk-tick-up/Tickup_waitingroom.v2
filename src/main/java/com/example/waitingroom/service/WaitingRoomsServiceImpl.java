package com.example.waitingroom.service;

import com.example.waitingroom.config.security.JwtTokenProvider;
import com.example.waitingroom.domain.WaitingRooms;
import com.example.waitingroom.repository.ParticipantRepository;
import com.example.waitingroom.repository.WaitingRoomRepository;
import com.example.waitingroom.request.CreateWaitingRoomRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WaitingRoomsServiceImpl implements WaitingRoomsService {
    private final WaitingRoomRepository waitingRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final ParticipantRepository participantRepository;


    private void validateUserToken(String token) {
        try {
            if (!jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            String userId = jwtTokenProvider.getUsername(token);
            if (userId == null) {
                throw new IllegalArgumentException("토큰에서 사용자 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("토큰 검증 실패: " + e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("유효한 토큰이 없습니다.");
    }

    //private room 만드는 API
    public WaitingRooms createWaitingGameRoom(CreateWaitingRoomRequest CWReq, HttpServletRequest httpRequest) {
        // JWT 토큰에서 유저 정보 추출
        String token = extractToken(httpRequest);
        validateUserToken(token);

        if(CWReq.GameType() == WaitingRooms.GameType.Contest && !CWReq.userRole().equals("Admin")) {
            throw new IllegalArgumentException("Contest 게임 방은 관리자만 생성할 수 있습니다.");
        }

        String gameType = CWReq.GameType().name();
        if(!WaitingRooms.GameType.checking(gameType)) {
            throw new IllegalArgumentException("지원하지 않는 게임 형식입니다.");
        }

        WaitingRooms newWaitingRoom = CWReq.toEntityPrivate();
        return waitingRoomRepository.save(newWaitingRoom);
    }

    //private room 참여하는 API
    public WaitingRooms joinPrivateWaitingRoom(String gameRoomCode, HttpServletRequest httpRequest) {
        WaitingRooms waitingRoom = waitingRoomRepository.findByGameRoomCode(gameRoomCode);
        validateUserSession(httpRequest);

        if(waitingRoom == null) throw new IllegalArgumentException("존재하지 않는 게임방입니다.");

        if(waitingRoom.getGameType() == WaitingRooms.GameType.Basic) throw new IllegalArgumentException("해당 게임방은 입장할 수 없습니다.");

        if(waitingRoom.isStarted()) throw new IllegalArgumentException("이미 게임이 시작한 방입니다.");

        if(!waitingRoom.incrementParticipants()) throw new IllegalArgumentException("방이 가득 찼습니다.");


        return waitingRoomRepository.save(waitingRoom);
    }

    //랜덤 공개방을 만들거나 참여하는 API
    public WaitingRooms createOrJoinPublicRoom(CreateWaitingRoomRequest req) {
//        validateUserSession(httpRequest);
        WaitingRooms.GameType GameType = null;

        if (req.GameType() == WaitingRooms.GameType.Contest && !req.userRole().equals("Admin")) {
            throw new IllegalArgumentException("Contest 게임 방은 관리자만 생성할 수 있습니다.");
        }

        if (!WaitingRooms.GameType.checking(String.valueOf(req.GameType()))) {
            throw new IllegalArgumentException("지원하지 않는 게임 형식입니다.");
        }

        WaitingRooms availbleRoom = waitingRoomRepository.findExistWaitingRooms();

        if(availbleRoom != null) {
            if(!availbleRoom.incrementParticipants()) {
                throw new IllegalArgumentException("방이 가득 찼습니다");
            }

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

    public void incrementParticipants(Long gameRoomId) {
        WaitingRooms waitingRoom = waitingRoomRepository.findWaitingRoomsById(gameRoomId);
        if (waitingRoom == null ) throw new IllegalArgumentException("존재하지 않는 방입니다.");
        if(!waitingRoom.incrementParticipants()) throw new IllegalArgumentException("참가자 수 증가 실패");

        waitingRoomRepository.save(waitingRoom);
        if(waitingRoom.getParticipants() == 0) waitingRoomRepository.deleteById(gameRoomId);

    }

    @Transactional
    public WaitingRooms decrementParticipants(Long gameRoomId) {
        WaitingRooms waitingRooms = waitingRoomRepository.findWaitingRoomsById(gameRoomId);
        if (waitingRooms == null ) throw new IllegalArgumentException("존재하지 않는 방입니다.");
        if (!waitingRooms.decrementParticipants()) throw new IllegalArgumentException("참가자 수 감소 실패");

        waitingRoomRepository.save(waitingRooms);

        if (waitingRooms.getParticipants() == 0 ) waitingRoomRepository.deleteById(gameRoomId);
        return waitingRooms;
    }


    public void validateUserSession(HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getSession().getAttribute("id");
        String userName = (String) httpRequest.getSession().getAttribute("nickname");

        if(userId == null || userName == null) {
            throw new IllegalArgumentException("세션에 유저 정보가 없습니다.");
        }
    }

//    public List<ParticipantsInfo> getParticipantsFromRedis(Long gameRoomId) {
//        return redisParticipantsRepository.getParticipants(gameRoomId);
//    }

//    public void addParticipantToRoom(Long gameRoomId, HttpServletRequest httpRequest) {
//        String userId = (String) httpRequest.getSession().getAttribute("userId");
//        String userName = (String) httpRequest.getSession().getAttribute("userName");
//
//        if (userId == null || userName == null) {
//            throw new IllegalArgumentException("세션에 유저 정보가 없습니다.");
//        }



//        List<ParticipantsInfo> participants = redisParticipantsRepository.getParticipants(gameRoomId);
//        participants.add(new ParticipantsInfo(userId, userName));
//        redisParticipantsRepository.saveParticipants(gameRoomId, participants);
//
//        messagingTemplate.convertAndSend(
//                "/topic/waiting-room/" + gameRoomId,
//                participants
//        );
//    }


//    public void removeParticipantsFromRoom(Long gameRoomId, String userId) {
//        if (userId == null || userId.isEmpty()) throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
//
//        List<ParticipantsInfo> participants = redisParticipantsRepository.getParticipants(gameRoomId);
//
//        boolean removed = participants.removeIf(p -> p.getUserId().equals(userId));
//
//        if (!removed) throw new IllegalArgumentException("참가자 목록에 해당 유저가 존재하지 않습니다.");
//
//        WaitingRooms waitingRoom = waitingRoomRepository.findById(gameRoomId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임 방입니다."));
//
//        waitingRoom.decrementParticipants();
//        waitingRoomRepository.save(waitingRoom);
//
//        messagingTemplate.convertAndSend(
//                "/topic/waiting-room/" + gameRoomId,
//                participants
//        );
//    }

    public WaitingRooms findRoomIdByCode(String gameRoomCode) {
        return waitingRoomRepository.findByGameRoomCode(gameRoomCode);
    }

    //Contest 방 만드는 API
    public WaitingRooms createContestWaitingRoom(CreateWaitingRoomRequest req) {

        if (!WaitingRooms.GameType.checking(String.valueOf(req.GameType()))) {
            throw new IllegalArgumentException("지원하지 않는 게임 형식입니다.");
        }
        if (req.GameType() == WaitingRooms.GameType.Contest && !req.userRole().equals("Admin")) {
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

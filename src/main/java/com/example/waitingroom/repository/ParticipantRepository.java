package com.example.waitingroom.repository;

import com.example.waitingroom.domain.ParticipantsInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ParticipantRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    private String generateKey(Long waitingRoomId){
        return "room_No." + waitingRoomId;
    }

    public void save(Long waitingRoomId, ParticipantsInfo participants) {
        String key = generateKey(waitingRoomId);
        redisTemplate.opsForHash().put(key, String.valueOf(participants.getOrderNum()), participants);
        redisTemplate.expire(key, Duration.ofDays(7));
    }

    public List<ParticipantsInfo> findAllByWaitingRoomId(Long waitingRoomId) {
        String key = generateKey(waitingRoomId);
        return new ArrayList<>(redisTemplate.opsForHash().values(key))
                .stream()
                .map(obj -> {
                    if (obj instanceof ParticipantsInfo) {
                        return (ParticipantsInfo) obj;
                    } else if (obj instanceof LinkedHashMap) {
                        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
                        return ParticipantsInfo.builder()
                                .orderNum((Integer) map.get("orderNum"))
                                .userId((String) map.get("userId"))
                                .nickname((String) map.get("nickname"))
                                .gameType((String) map.get("gameType"))
                                .waitingRoomId(Long.parseLong(map.get("waitingRoomId").toString()))
                                .userStatus((String) map.get("userStatus"))
                                .build();
                    } else {
                        throw new IllegalStateException("Unexpected data type: " + obj.getClass());
                    }
                })
                .toList();
    }

    public void deleteByWaitingRoomIdAndParticipantsId(Long waitingRoomId, String participantsId) {
        String key = generateKey(waitingRoomId);
        // 현재 참가자 목록 조회
        List<ParticipantsInfo> participants = findAllByWaitingRoomId(waitingRoomId);

        // 삭제할 참가자 찾기
        ParticipantsInfo targetParticipant = participants.stream()
                .filter(p -> p.getUserId().equals(participantsId))
                .findFirst()
                .orElse(null);

        if (targetParticipant != null) {
            redisTemplate.opsForHash().delete(key, String.valueOf(targetParticipant.getOrderNum()));

            List<ParticipantsInfo> remainingParticipants = findAllByWaitingRoomId(waitingRoomId);
            if (remainingParticipants.isEmpty()) {
                redisTemplate.delete(key);
            }
        }
    }

//    public void deleteAllByRoomId(Long roomId) {
//        String key = generateKey(roomId);
//        redisTemplate.delete(key);
//    }
}

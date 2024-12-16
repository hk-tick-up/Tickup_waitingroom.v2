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

    private String generateKey(Long roomId){
            return "room_No." + roomId;
        }

    public void save(Long roomId, ParticipantsInfo participants) {
        String key = generateKey(roomId);
        redisTemplate.opsForHash().put(key, String.valueOf(participants.getOrderNum()), participants);
        redisTemplate.expire(key, Duration.ofDays(7));
    }

//    public Integer findMaxOrderNumByRoomId(Long roomId) {
//        String key = generateKey(roomId);
//        List<Object> participantsData = new ArrayList<>(redisTemplate.opsForHash().values(key));
//
//        return participantsData.stream()
//                .map(obj -> {
//                    if (obj instanceof ParticipantsInfo) {
//                        return ((ParticipantsInfo) obj).getOrderNum();
//                    } else if (obj instanceof LinkedHashMap) {
//                        return (Integer) ((LinkedHashMap) obj).get("orderNum");
//                    } else {
//                        throw new IllegalStateException("Unexpected data type: " + obj.getClass());
//                    }
//                })
//                .max(Integer::compareTo)
//                .orElse(0);
//    }

    public List<ParticipantsInfo> findAllByRoomId(Long roomId) {
        String key = generateKey(roomId);
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
                                .currentRoomId(Long.parseLong(map.get("currentRoomId").toString()))
                                .userStatus((String) map.get("userStatus")) // userStatus 필드 추가
                                .build();
                    } else {
                        throw new IllegalStateException("Unexpected data type: " + obj.getClass());
                    }
                })
                .toList();
    }

    public void deleteByRoomIdAndParticipantsId(Long roomId, String participantsId) {
        String key = generateKey(roomId);
        // 현재 참가자 목록 조회
        List<ParticipantsInfo> participants = findAllByRoomId(roomId);

        // 삭제할 참가자 찾기
        ParticipantsInfo targetParticipant = participants.stream()
                .filter(p -> p.getUserId().equals(participantsId))
                .findFirst()
                .orElse(null);

        if (targetParticipant != null) {
            redisTemplate.opsForHash().delete(key, String.valueOf(targetParticipant.getOrderNum()));

            List<ParticipantsInfo> remainingParticipants = findAllByRoomId(roomId);
            if (remainingParticipants.isEmpty()) {
                redisTemplate.delete(key);
            }
        }
    }

    public void deleteAllByRoomId(Long roomId) {
        String key = generateKey(roomId);
        redisTemplate.delete(key);
    }
}

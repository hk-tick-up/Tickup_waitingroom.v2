package com.example.waitingroom.repository;

import com.example.waitingroom.domain.ParticipantsInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisParticipantsRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveParticipants(Long waitingRoomId, List<ParticipantsInfo> participants) {
        String key =  "waitingRoom: " + waitingRoomId;
        try {
            String value = objectMapper.writeValueAsString(participants);
            redisTemplate.opsForValue().set(key,value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("참가자 정보를 저장하는 중 문제가 발생하였습니다.");
        }
    }

    public List<ParticipantsInfo> getParticipants(Long waitingRoomId) {
        String key = "waitingRoom : " + waitingRoomId;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(value, new TypeReference<List<ParticipantsInfo>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("참가자 정보를 저장하는 중 문제가 발생하였습니다.");
        }
    }

    public void deleteParticipants(Long waitingRoomId) {
        String key = "waitingRoom: " + waitingRoomId;
        redisTemplate.delete(key);
    }
}

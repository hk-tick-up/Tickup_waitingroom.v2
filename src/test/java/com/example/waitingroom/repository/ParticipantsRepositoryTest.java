package com.example.waitingroom.repository;

import com.example.waitingroom.domain.ParticipantsInfo;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.*;

@DataRedisTest
public class ParticipantsRepositoryTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private ParticipantRepository participantRepository;

    public void ParticipantRepositoryTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveParticipantsToRedis() {
        // Given
        Long roomId = 1L;
        ParticipantsInfo participant = ParticipantsInfo.builder()
                .orderNum(1)
                .userId("user123")
                .nickname("TestUser")
                .gameType("Basic")
                .currentRoomId(roomId)
                .build();

        String key = "room:" + roomId; // generateKey(roomId) 결과

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        // When
        participantRepository.save(roomId, participant);

        // Then
        verify(hashOperations, times(1)).put(key, participant.getOrderNum(), participant);
    }
}
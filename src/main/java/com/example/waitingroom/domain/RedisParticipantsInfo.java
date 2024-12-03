package com.example.waitingroom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("participants")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RedisParticipantsInfo {
    private String userId;
    private String nickname;
    private String userStauts;
    private Integer orderNum;
    private Long WatingRoomId;

    public RedisParticipantsInfo(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
        this.userStauts = "대기중";
    }
}

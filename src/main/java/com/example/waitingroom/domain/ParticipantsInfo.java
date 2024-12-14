package com.example.waitingroom.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsInfo {

    @Column(name="order_number")
    private Integer orderNum;
    @Column(name = "user_id")
    private String userId;
    @Column(name="user_nick_name")
    private String nickname;
    @Column(name="game_type")
    private String gameType;
    @Column(name="waiting_room_id")
    private Long currentRoomId;

    public ParticipantsInfo(String userId, String nickname, Integer orderNum) {
        this.userId = userId;
        this.nickname = nickname;
        this.orderNum = orderNum;
    }

    public ParticipantsInfo participantsInfoOrderNum(Integer newOrderNum) {
        return ParticipantsInfo.builder()
                .orderNum(newOrderNum)
                .userId(this.userId)
                .nickname(this.nickname)
                .gameType(this.gameType)
                .currentRoomId(this.currentRoomId)
                .build();
    }
}

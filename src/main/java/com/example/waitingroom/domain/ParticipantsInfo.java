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
    private Long waitingRoomId;
    @Column(name="user_status")
    private String userStatus;

    public ParticipantsInfo(String userId, String nickname, Integer orderNum, String userStatus) {
        this.userId = userId;
        this.nickname = nickname;
        this.orderNum = orderNum;
        this.userStatus = userStatus;
    }

    public ParticipantsInfo participantsInfoOrderNum(Integer newOrderNum) {
        return ParticipantsInfo.builder()
                .orderNum(newOrderNum)
                .userId(this.userId)
                .nickname(this.nickname)
                .gameType(this.gameType)
                .waitingRoomId(this.waitingRoomId)
                .userStatus(this.userStatus)
                .build();
    }

    public ParticipantsInfo updateUserStatus(String newStatus) {
        return ParticipantsInfo.builder()
                .orderNum(this.orderNum)
                .userId(this.userId)
                .nickname(this.nickname)
                .gameType(this.gameType)
                .waitingRoomId(this.waitingRoomId)
                .userStatus(newStatus)
                .build();
    }
}
package com.example.waitingroom.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;


//MySQL에 저장할 "시작한" 방 기준 데이터
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paritipants_id")
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name="user_nick_name")
    private String nickname;
    @Column(name="game_type")
    private String gameType;
    @Column(name="is_public")
    private boolean isPublic;

    public ParticipantsInfo(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }



}

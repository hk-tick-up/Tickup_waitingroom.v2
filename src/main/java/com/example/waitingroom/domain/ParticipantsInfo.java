package com.example.waitingroom.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String userNickName;
    @Column(name="game_type")
    private String gameType;
    @Column(name="is_public")
    private boolean isPublic;
}

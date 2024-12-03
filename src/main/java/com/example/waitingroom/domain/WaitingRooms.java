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
public class WaitingRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="waiting_room_id")
    private Long id;
    @Column(name="waiting_room_code")
    private String gameRoomCode;
    @Enumerated(EnumType.STRING)
    @Column(name="game_type")
    private GameType gameType;
    private int participants;
    @Column(name="max_participants")
    private Integer maxParticipants;
    @Column(name="is_public", updatable = false)
    private boolean isPublic;
    @Column(name="is_started")
    private boolean isStarted;

    public enum GameType {
        Basic,
        Contest;

        public static boolean checking(String type) {
            try {
                GameType.valueOf(type);
            }catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        }
    }

    public boolean incrementParticipants(){
        if(this.participants < this.maxParticipants) {
            this.participants++;
            return true;
        }
        return false;
    }

    public boolean decrementParticipants() {
        if (this.participants > 0) {
            this.participants--;
            return true;
        }
        return false;
    }
}

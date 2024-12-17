package com.example.waitingroom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameStartMessageDto {
    private String type;
    private String gameRoomId;
    private Long gameRoomsId;
}

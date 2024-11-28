package com.example.waitingroom.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WaitingRoomSocketController {
    @MessageMapping("/sendMessage")
    @SendTo("/topic/{gameRoomId}")
    public String sendMessage(String message){
        return message;
    }
}

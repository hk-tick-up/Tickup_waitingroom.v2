package com.example.waitingroom.controller;

import com.example.waitingroom.DTO.ParticipantResponseDto;
import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/participants")
@RequiredArgsConstructor
public class ParticipantTransmitController {
    private final ParticipantService participantService;

    @GetMapping("/{waitingRoomId}")
    public ResponseEntity<List<ParticipantResponseDto>> getParticipants(@PathVariable Long waitingRoomId) {
        List<ParticipantsInfo> participants = participantService.participantsList(waitingRoomId);
        List<ParticipantResponseDto> response = participants.stream()
                .map(ParticipantResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{waitingRoomId}/participants/{userId}")
    public ResponseEntity<ParticipantResponseDto> getParticipant(
            @PathVariable Long waitingRoomId,
            @PathVariable String userId) {
        List<ParticipantsInfo> participants = participantService.participantsList(waitingRoomId);
        return participants.stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .map(participant -> ResponseEntity.ok(ParticipantResponseDto.from(participant)))
                .orElse(ResponseEntity.notFound().build());
    }
}

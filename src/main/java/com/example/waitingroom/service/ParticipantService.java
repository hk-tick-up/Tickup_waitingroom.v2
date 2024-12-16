package com.example.waitingroom.service;

import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    private void validateParticipant(ParticipantsInfo participants) {
        if(participants.getUserId() == null || participants.getNickname() == null ){
            throw new IllegalArgumentException("참가자 정보가 유효하지 않습니다.");
        }
    }

    public void addParticipant(Long roomId, ParticipantsInfo participants) {
        validateParticipant(participants);

        List<ParticipantsInfo> currentParticipants = participantRepository.findAllByRoomId(roomId);

        boolean isAlreadyParticipant = currentParticipants.stream()
                .anyMatch(p -> p.getUserId().equals(participants.getUserId()));

        if (isAlreadyParticipant) {
            return;
        }

        if (currentParticipants.size() >= 5) {
            throw new IllegalStateException("방이 가득 찼습니다");
        }

        Integer newOrderNum = currentParticipants.size() + 1;
        ParticipantsInfo newParticipant = participants.participantsInfoOrderNum(newOrderNum);
        if (newParticipant.getUserStatus() == null) {
            newParticipant = newParticipant.updateUserStatus("대기중");
        }
        participantRepository.save(roomId, newParticipant);
    }
    
//    public List<ParticipantsInfo> removeParticipants(Long roomId, String participantsId) {
//        participantRepository.deleteByRoomIdAndParticipantsId(roomId, participantsId);
//        return participantRepository.findAllByRoomId(roomId);
//    }
    public void removeParticipants(Long roomId, String participantsId) {
        participantRepository.deleteByRoomIdAndParticipantsId(roomId, participantsId);
    }

    public void updateParticipantStatus(Long gameRoomId, String userId, String newStatus) {
        List<ParticipantsInfo> participants = participantRepository.findAllByRoomId(gameRoomId);

        ParticipantsInfo participantToUpdate = participants.stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("참가자를 찾을 수 없습니다."));

        ParticipantsInfo updatedParticipant = participantToUpdate.updateUserStatus(newStatus);
        participantRepository.save(gameRoomId, updatedParticipant);
    }

    public List<ParticipantsInfo> participantsList(Long roomId) {
        return participantRepository.findAllByRoomId(roomId);
    }

}

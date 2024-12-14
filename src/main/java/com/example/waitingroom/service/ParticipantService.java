package com.example.waitingroom.service;

import com.example.waitingroom.domain.ParticipantsInfo;
import com.example.waitingroom.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // 이미 참가한 사용자인지 확인
        boolean isAlreadyParticipant = currentParticipants.stream()
                .anyMatch(p -> p.getUserId().equals(participants.getUserId()));

        if (isAlreadyParticipant) {
            return; // 이미 참가한 사용자면 추가하지 않음
        }

        if (currentParticipants.size() >= 5) {
            throw new IllegalStateException("방이 가득 찼습니다");
        }

        Integer newOrderNum = currentParticipants.size() + 1;
        ParticipantsInfo newParticipant = participants.participantsInfoOrderNum(newOrderNum);
        participantRepository.save(roomId, newParticipant);
    }
    
//    public List<ParticipantsInfo> removeParticipants(Long roomId, String participantsId) {
//        participantRepository.deleteByRoomIdAndParticipantsId(roomId, participantsId);
//        return participantRepository.findAllByRoomId(roomId);
//    }
    public void removeParticipants(Long roomId, String participantsId) {
        participantRepository.deleteByRoomIdAndParticipantsId(roomId, participantsId);
    }

    public List<ParticipantsInfo> participantsList(Long roomId) {
        return participantRepository.findAllByRoomId(roomId);
    }


}

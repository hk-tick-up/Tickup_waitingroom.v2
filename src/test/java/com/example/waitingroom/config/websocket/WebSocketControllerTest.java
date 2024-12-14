//package com.example.waitingroom.config.websocket;
//
//import com.example.waitingroom.domain.ParticipantsInfo;
//import com.example.waitingroom.service.ParticipantService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//class WebSocketControllerTest {
//
//    @MockBean
//    private ParticipantService participantService;
//
//    private WebSocketController webSocketController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
////        webSocketController = new WebSocketController(participantService);
//    }
//
//    @Test
//    void handleOtherUserJoined_ValidParticipant_ReturnsUserJoinedResponse() {
//        // given
//        Long gameRoomId = 1L;
//        ParticipantsInfo participant = new ParticipantsInfo();
//        participant.setUserId("user1");
//        participant.setNickname("nickname1");
//
//        List<ParticipantsInfo> currentParticipants = new ArrayList<>();
//        currentParticipants.add(participant);
//
//        when(participantService.addParticipant(gameRoomId, participant)).thenReturn(currentParticipants);
//
//        // when
//        Map<String, Object> response = webSocketController.handleOtherUserJoined(gameRoomId, participant);
//
//        // then
//        assertNotNull(response);
//        assertEquals("USER_JOINED", response.get("type"));
//        assertEquals(participant, response.get("participantInfo"));
//        assertEquals(1, participant.getOrderNum());
//    }
//
//    @Test
//    void handleOtherUserJoined_ExceptionOccurs_ReturnsErrorResponse() {
//        // given
//        Long gameRoomId = 1L;
//        ParticipantsInfo participant = new ParticipantsInfo();
//
//        when(participantService.addParticipant(any(Long.class), any(ParticipantsInfo.class)))
//                .thenThrow(new RuntimeException("Failed to add participant"));
//
//        // when
//        Map<String, Object> response = webSocketController.handleOtherUserJoined(gameRoomId, participant);
//
//        // then
//        assertNotNull(response);
//        assertEquals("ERROR", response.get("type"));
//        assertEquals("Failed to register participant", response.get("message"));
//    }
//}
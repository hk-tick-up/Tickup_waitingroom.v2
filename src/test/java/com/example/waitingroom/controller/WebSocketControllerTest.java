//package com.example.waitingroom.controller;
//
//import com.example.waitingroom.config.websocket.WebSocketController;
//import com.example.waitingroom.domain.ParticipantsInfo;
//import com.example.waitingroom.service.ParticipantService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.simp.stomp.*;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.web.socket.sockjs.client.SockJsClient;
//import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//
//import java.lang.reflect.Type;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class WebSocketControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private WebSocketController webSocketController;
//
//    @MockBean
//    private ParticipantService participantService;
//
//    private WebSocketStompClient stompClient;
//
//    @BeforeEach
//    public void setup() {
//        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
//        WebSocketTransport webSocketTransport = new WebSocketTransport(webSocketClient);
//        SockJsClient sockJsClient = new SockJsClient(Collections.singletonList(webSocketTransport));
//
//        this.stompClient = new WebSocketStompClient(sockJsClient);
//        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//    }
//
//    @Test
//    public void testHandleUserJoined() throws Exception {
//        Long gameRoomId = 1L;
//        ParticipantsInfo participant = new ParticipantsInfo("user1", "nickname1");
//        ParticipantsInfo savedParticipant = new ParticipantsInfo("user1", "nickname1");
////        savedParticipant.setId(1L);
//
//        when(participantService.addParticipant(eq(gameRoomId), any(ParticipantsInfo.class)))
//                .thenReturn((List<ParticipantsInfo>) savedParticipant);
//
//        CompletableFuture<UserJoinedMessage> completableFuture = new CompletableFuture<>();
//
//        StompSession session = stompClient
//                .connect(String.format("ws://localhost:%d/ws", port), new StompSessionHandlerAdapter() {})
//                .get(1, TimeUnit.SECONDS);
//
//        session.subscribe(String.format("/topic/waiting-room/%d", gameRoomId), new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return UserJoinedMessage.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                completableFuture.complete((UserJoinedMessage) payload);
//            }
//        });
//
//        session.send(String.format("/app/waiting-room/%d", gameRoomId), participant);
//
//        UserJoinedMessage result = completableFuture.get(5, TimeUnit.SECONDS);
//
//        assertEquals(savedParticipant.getId(), result.getParticipant().getId());
//        assertEquals(savedParticipant.getUserId(), result.getParticipant().getUserId());
//        assertEquals(savedParticipant.getNickname(), result.getParticipant().getNickname());
//    }
//
//    static class UserJoinedMessage {
//        private ParticipantsInfo participant;
//
//        public UserJoinedMessage() {}
//
//        public UserJoinedMessage(ParticipantsInfo participant) {
//            this.participant = participant;
//        }
//
//        public ParticipantsInfo getParticipant() {
//            return participant;
//        }
//
//        public void setParticipant(ParticipantsInfo participant) {
//            this.participant = participant;
//        }
//    }
//}
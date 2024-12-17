//package com.example.waitingroom.config.websocket;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class WaitingRoomWebSocketHandler
////        extends TextWebSocketHandler
//{
//    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
//
////    @Override
////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
////        String payload = message.getPayload();
////        ObjectMapper objectMapper = new ObjectMapper();
////        JsonNode jsonNode = objectMapper.readTree(payload);
////
////        String type = jsonNode.get("type").asText();
////        if ("JOIN_ROOM".equals(type)) {
////            handleJoinRoom(session, jsonNode);
////        }
////        // 다른 이벤트 처리 추가
////    }
//
////    private void handleJoinRoom(WebSocketSession session, JsonNode jsonNode) {
////        String userId = jsonNode.get("userId").asText();
////        String nickname = jsonNode.get("nickname").asText();
////        String roomId = jsonNode.get("roomId").asText();
////
////        // 세션 정보 저장
////        sessionMap.put(userId, session);
////
////        // 추가 로직 구현(예: 참가자 목록 업데이트 등)
////        System.out.println("User joined room: userId=" + userId + ", nickname=" + nickname + ", roomId=" + roomId);
////    }
//}
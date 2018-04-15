package com.example.springbootwebsocket.websocket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat-room/{username}")
public class ChatRoomServerEndpoint {

    private static Map<String, Session> livingSessions = new ConcurrentHashMap<String, Session>();


    @OnOpen
    public void openSession(@PathParam("username") String username, Session session) {

        String sessionId = session.getId();

        livingSessions.put(sessionId, session);


        sendText(session, "欢迎用户【 " + username + "] 来到聊天室");

    }

    @OnMessage
    public void onMessage(@PathParam("username") String username, Session session, String message) {

        // sendText(session, "用户【" + username + "] : " + message);

        sendTestAll("用户：【 " + username + " 】 :" + message);

    }

    public void sendTestAll(String message) {

        livingSessions.forEach((sessionId, session) -> {
            sendText(session, message);
        });
    }

    @OnClose
    public void onClose(@PathParam("username") String username, Session session) {

        String sessionID = session.getId();
        livingSessions.remove(sessionID);

        sendTestAll("用户【 " + username + "】已经离开聊天室了");


    }


    private void sendText(Session session, String message) {

        RemoteEndpoint.Basic basic = session.getBasicRemote();

        try {
            basic.sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}





















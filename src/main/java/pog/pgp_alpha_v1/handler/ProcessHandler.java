package pog.pgp_alpha_v1.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class ProcessHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 处理从客户端接收到的消息
    }

    public void sendProgressUpdate(WebSocketSession session, String progress) {
        try {
            session.sendMessage(new TextMessage(progress));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

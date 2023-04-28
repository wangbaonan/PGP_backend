package pog.pgp_alpha_v1.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pog.pgp_alpha_v1.events.AnalysisProgressEvent;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public MyHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = session.getAttributes().get("userId").toString();
        String analysisId = session.getAttributes().get("analysisId").toString();
        String key = userId + "_" + analysisId;
        sessions.put(key, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = session.getAttributes().get("userId").toString();
        String analysisId = session.getAttributes().get("analysisId").toString();
        String key = userId + "_" + analysisId;
        sessions.remove(key);
    }

    public WebSocketSession getSessionByUserIdAndAnalysisId(Long userId, Long analysisId) {
        String key = userId + "_" + analysisId;
        return sessions.get(key);
    }

    @EventListener
    public void onAnalysisProgress(AnalysisProgressEvent event) {
        sendProgressUpdate(event.getUserId(), event.getAnalysisId(), event.getProgress());
    }

    private void sendProgressUpdate(Long userId, Long analysisId, String progress) {
        WebSocketSession session = getSessionByUserIdAndAnalysisId(userId, analysisId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(progress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



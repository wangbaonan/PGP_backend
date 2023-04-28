package pog.pgp_alpha_v1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import pog.pgp_alpha_v1.handler.MyHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyHandler myHandler;

    @Autowired
    public WebSocketConfig(MyHandler myHandler) {
        this.myHandler = myHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler, "/ws/progress/{userId}/{analysisId}")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    private Map<String, Object> determineUserAttributes(WebSocketSession session) {
                        // 从WebSocketSession中获取用户ID和分析ID
                        URI uri = session.getUri();
                        Map<String, String> queryParameters = getQueryParameters(uri);

                        Long userId = Long.parseLong(queryParameters.get("userId"));
                        Long analysisId = Long.parseLong(queryParameters.get("analysisId"));

                        // 将用户ID和分析ID存储到WebSocketSession的attributes中
                        Map<String, Object> userAttributes = new HashMap<>();
                        userAttributes.put("userId", userId);
                        userAttributes.put("analysisId", analysisId);

                        return userAttributes;
                    }

                })
                .setAllowedOrigins("*");
    }

    private Map<String, String> getQueryParameters(URI uri) {
        String query = uri.getQuery();
        Map<String, String> queryParameters = new HashMap<>();

        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                queryParameters.put(pair.substring(0, idx), pair.substring(idx + 1));
            }
        }

        return queryParameters;
    }

}

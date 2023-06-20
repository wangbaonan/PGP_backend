package pog.pgp_alpha_v1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
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
        HandshakeInterceptor interceptor = new HttpSessionHandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    URI uri = servletRequest.getURI();
                    String[] pathSegments = uri.getPath().split("/");
                    String userId = pathSegments[pathSegments.length - 2];
                    String analysisId = pathSegments[pathSegments.length - 1];
                    attributes.put("userId", userId);
                    attributes.put("analysisId", analysisId);
                }
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        };

        registry.addHandler(myHandler, "/ws/progress/{userId}/{analysisId}")
                .addInterceptors(interceptor)
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

package com.example.onlybunsbe.Config;

import com.example.onlybunsbe.Interceptor.JwtHandshakeInterceptor;
import com.example.onlybunsbe.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final WebSocketHandler myWebSocketHandler;

    @Autowired
    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor, WebSocketHandler WebSocketHandler) {
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
        this.myWebSocketHandler = WebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("Registering WebSocket handler for /api/chat");
        registry.addHandler(myWebSocketHandler, "/api/chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor);
    }
}

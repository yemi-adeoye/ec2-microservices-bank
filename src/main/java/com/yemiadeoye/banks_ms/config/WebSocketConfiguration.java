package com.yemiadeoye.banks_ms.config;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.socket.client.IO;
import io.socket.client.Socket;

@Configuration
public class WebSocketConfiguration {
    @Value("${gateway.url}")
    private String gatewayUrl;

    @Bean
    Socket createsocket() throws URISyntaxException {
        Socket socket = IO.socket("http://localhost:7000");
        socket.connect();
        return socket;
    }
}

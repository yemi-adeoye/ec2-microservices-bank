package com.yemiadeoye.banks_ms.config;

import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.socket.client.IO;
import io.socket.client.Socket;

@Configuration
public class WebSocketConfiguration {
    @Bean
    Socket createsocket() throws URISyntaxException {
        Socket socket = IO.socket("http://localhost:7000");
        socket.connect();
        return socket;
    }
}

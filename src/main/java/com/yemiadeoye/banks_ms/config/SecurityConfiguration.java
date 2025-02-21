package com.yemiadeoye.banks_ms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Value("${keyset.uri}")
    private String keySetUri;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(customizer -> customizer.jwt(jwtCustomizer -> jwtCustomizer.jwkSetUri(keySetUri)));
        http.csrf(csrfCustomizer -> csrfCustomizer.disable());
        http.authorizeHttpRequests(authCustomizer -> authCustomizer.anyRequest().authenticated());

        return http.build();
    }
}

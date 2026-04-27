package com.bawantha.microSpring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        // Public pages
                        .pathMatchers("/", "/login", "/favicon.ico").permitAll()
                        // Allow item CRUD without auth (for testing/admin use)
                        .pathMatchers(HttpMethod.POST, "/item").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/item/**").permitAll()
                        // Cart operations require login
                        .anyExchange().authenticated()
                )
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .build();
    }

}

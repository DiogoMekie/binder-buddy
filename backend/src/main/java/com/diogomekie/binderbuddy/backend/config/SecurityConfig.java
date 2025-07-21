package com.diogomekie.binderbuddy.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Import HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
// You might also need this import if you keep PathRequest.toStaticResources()
// import org.springframework.boot.autoconfigure.security.servlet.PathRequest;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for now (common for REST APIs)
                .authorizeHttpRequests(authorize -> authorize
                        // Permit all requests to /api/hello for GET method
                        .requestMatchers(HttpMethod.GET, "/api/hello").permitAll()
                        // Permit all OPTIONS requests (for CORS preflight) to any path
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // If you have static resources (like login.html, etc.) that need to be public:
                        // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // Use HTTP Basic for other protected endpoints

        return http.build();
    }
}
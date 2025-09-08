package com.skillmentor.root.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@Profile("dev") // <-- Only active in dev
public class SecurityConfigDev {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS + allow preflight
                .cors(Customizer.withDefaults())
                // Disable CSRF for APIs in dev
                .csrf(csrf -> csrf.disable())
                // Everything is allowed
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // Keep API stateless
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // NOTE: Do NOT add your JwtAuthenticationFilter here in dev.
        // e.g., no ".addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)"

        return http.build();
    }

    // Global CORS allowing Vite dev server (and 127.0.0.1 variant). Handles OPTIONS.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        // If you rely on cookies with Clerk in dev, set true; for pure Bearer tokens it's fine either way.
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L); // Cache preflight for 1h

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}

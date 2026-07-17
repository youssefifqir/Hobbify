package com.example.hobbify.config.security;

import com.example.hobbify.config.security.JwtFilter;
import com.example.hobbify.config.security.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh",
            "/api/v1/auth/login/anonymous",
            "/api/v1/auth/whoami",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/",
            "/index.html",
            "/favicon.ico",
            "/error",
            "/actuator/**"
            ,
            "/api/v1/hobby/public",
            "/api/v1/stage/public",
            "/api/v1/step/public",
            "/api/v1/achievement/public"
            ,
            "/api/v1/catalog/**"
    };

    private final JwtFilter jwtFilter;
    private final RateLimitFilter rateLimitFilter;
    private final com.example.hobbify.config.security.authz.AnonymousPrincipalSupport anonymousBinding;

    // Comma-separated origin patterns. Localhost/127.0.0.1 (any port) always allowed
    // for local dev regardless of this value. In production, set CORS_ALLOWED_ORIGINS
    // to the deployed frontend's real origin (e.g. the Vercel URL) — nothing else will
    // be able to read authenticated responses from the browser without it.
    @Value("${app.cors.allowed-origins:}")
    private String extraAllowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final List<String> origins = new java.util.ArrayList<>(List.of(
                "http://localhost:*", "https://localhost:*",
                "http://127.0.0.1:*", "https://127.0.0.1:*"
        ));
        if (extraAllowedOrigins != null && !extraAllowedOrigins.isBlank()) {
            for (final String origin : extraAllowedOrigins.split(",")) {
                if (!origin.isBlank()) {
                    origins.add(origin.trim());
                }
            }
        }

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "X-Forwarded-For"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .headers(headers -> headers
                        .xssProtection(xss -> xss.headerValue(org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                        .frameOptions(frame -> frame.deny())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)))
                .sessionManagement(sess -> sess.sessionCreationPolicy(STATELESS))
                .addFilterBefore(this.rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.anonymousBinding, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}



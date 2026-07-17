package com.example.hobbify.config;

import com.example.hobbify.common.util.KeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Configuration
@EnableCaching
@EnableAsync
@Slf4j
public class BeansConfig {

    private KeyPair cachedKeyPair;

    // Set on any host with an ephemeral/read-only filesystem (Render, most PaaS free
    // tiers): without these, a restart or scale-to-zero generates a brand-new RSA key
    // pair and silently invalidates every issued access/refresh token. Base64-encode
    // the full PEM file content, e.g. `base64 -w0 private_key.pem`.
    @Value("${JWT_PRIVATE_KEY_B64:}")
    private String jwtPrivateKeyB64;

    @Value("${JWT_PUBLIC_KEY_B64:}")
    private String jwtPublicKeyB64;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditorAware();
    }

    @Bean
    public PrivateKey privateKey() throws Exception {
        return getOrGenerateKeyPair().getPrivate();
    }

    @Bean
    public PublicKey publicKey() throws Exception {
        return getOrGenerateKeyPair().getPublic();
    }

    private synchronized KeyPair getOrGenerateKeyPair() throws Exception {
        if (cachedKeyPair != null) {
            return cachedKeyPair;
        }

        if (!jwtPrivateKeyB64.isBlank() && !jwtPublicKeyB64.isBlank()) {
            final String privatePem = new String(Base64.getDecoder().decode(jwtPrivateKeyB64), StandardCharsets.UTF_8);
            final String publicPem = new String(Base64.getDecoder().decode(jwtPublicKeyB64), StandardCharsets.UTF_8);
            final PrivateKey privateKey = KeyUtils.parsePrivateKeyPem(privatePem);
            final PublicKey publicKey = KeyUtils.parsePublicKeyPem(publicPem);
            cachedKeyPair = new KeyPair(publicKey, privateKey);
            log.info("Loaded RSA key pair from JWT_PRIVATE_KEY_B64 / JWT_PUBLIC_KEY_B64");
            return cachedKeyPair;
        }

        try {
            // Try to load existing keys
            PrivateKey privateKey = KeyUtils.loadPrivateKey("keys/private_key.pem");
            PublicKey publicKey = KeyUtils.loadPublicKey("keys/public_key.pem");
            cachedKeyPair = new KeyPair(publicKey, privateKey);
            log.info("Successfully loaded RSA key pair from classpath");
            return cachedKeyPair;
        } catch (IllegalArgumentException e) {
            // Keys not found, generate new key pair
            log.info("RSA keys not found on classpath, generating new key pair...");
            cachedKeyPair = generateKeyPair();
            try {
                // Best-effort: only works on a writable, persistent filesystem (local dev).
                // On an ephemeral host this key pair will not survive a restart unless
                // JWT_PRIVATE_KEY_B64 / JWT_PUBLIC_KEY_B64 are set instead.
                KeyUtils.saveKeyPair(cachedKeyPair, "keys/private_key.pem", "keys/public_key.pem");
                log.info("RSA key pair generated and saved successfully");
            } catch (Exception saveError) {
                log.warn("Could not persist generated RSA key pair to disk (read-only or ephemeral filesystem?). " +
                        "Tokens will be invalidated on next restart unless JWT_PRIVATE_KEY_B64 / JWT_PUBLIC_KEY_B64 are set. Cause: {}",
                        saveError.getMessage());
            }
            return cachedKeyPair;
        }
    }

    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}

package com.example.hobbify.config.security;

public interface TokenDenylist {

    void add(String jti, long ttlSeconds);

    boolean isDenied(String jti);
}

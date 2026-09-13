package com.sawiya.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility component responsible for generating JSON Web Tokens (JWTs)
 * used for user authentication.
 *
 * <p>The signing key is initialized from the configured JWT secret and
 * is used to cryptographically sign generated tokens.</p>
 *
 * @author Achintha Kalunayaka
 * @since 8/20/2025
 */

@Component
public class JWTUtilityComponent {

    private final SecretKey key;

    /**
     * Creates a JWT utility component using the configured secret key.
     *
     * @param secret the Base64-encoded secret used to sign JWTs
     */
    public JWTUtilityComponent(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a signed JWT for the specified username.
     *
     * <p>The generated token contains the username as its subject,
     * the issue timestamp, and an expiration time of one hour from
     * the time of generation.</p>
     *
     * @param username the username to include as the token subject
     * @return a signed JWT containing the user's authentication information
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        Date.from(
                                Instant.now().plus(1, ChronoUnit.HOURS)
                        )
                )
                .signWith(key)
                .compact();
    }
}
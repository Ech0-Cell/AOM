package org.mth3902.aom.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {
    // Secret key for signing JWT
    private final String secretKey;

    @Autowired
    public AuthenticationService(Environment env) {
        this.secretKey = env.getProperty("auth.secret");
    }

    // Expiration time for JWT token (in milliseconds)
    private static final long EXPIRATION_TIME = 86400000 * 7; // 1 week

    // Method to generate JWT token from MSISDN
    public String generateToken(String msisdn) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(msisdn)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // Method to check if token is valid and compare MSISDN
    public boolean isValidToken(String token, String msisdn) {
        try {

            Claims body = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenMsisdn = body.getSubject();

            return tokenMsisdn.equals(msisdn);
        } catch (Exception e) {
            return false;
        }
    }

}

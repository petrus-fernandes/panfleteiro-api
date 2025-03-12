package br.com.promo.panfleteiro.infra.security;

import br.com.promo.panfleteiro.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TokenService {

    @Value("${api.security.token.secret.jwt}")
    private String secret;
    public String generateToken(User user) {
        try {
            Algorithm jwtAlgorithm =  Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("panfleteiro")
                    .withSubject(user.getUsername())
                    .withExpiresAt(getExpirationDate())
                    .sign(jwtAlgorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error creating token" + e.getMessage());
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm jwtAlgorithm =  Algorithm.HMAC256(secret);
            return JWT.require(jwtAlgorithm)
                    .withIssuer("panfleteiro")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (
                JWTVerificationException e) {
            return null;
        }
    }


    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).plusMinutes(30).toInstant(java.time.ZoneOffset.of("-03:00"));
    }
}

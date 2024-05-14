package org.puig.api.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.puig.api.util.PuigUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}") private String SECRET;
    @Value("${jwt.expires-in}") private long EXPIRES_IN;

    public String generateToken(@NonNull PuigUser puigUser) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRES_IN);

        return Jwts.builder()
                .subject(String.valueOf(puigUser.getId()))
                .claim("type", puigUser.getEspecializado())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key())
                .compact();
    }

    public SecretKey key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validate(String token, @NonNull PuigUser user) {
        final ObjectId username = new ObjectId(claims(token).getSubject());
        return username.equals(user.getId()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = claims(token).getExpiration();
        return expirationDate.before(new Date());
    }

    public long getExpiration(String token) {
        return claims(token).getExpiration().getTime();
    }
}

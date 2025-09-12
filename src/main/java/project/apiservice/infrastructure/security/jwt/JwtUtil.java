package project.apiservice.infrastructure.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.apiservice.domain.enums.UserRole;
import project.apiservice.domain.model.BlackListEntity;
import project.apiservice.infrastructure.persistance.BlackListRepositoryJpa;
import project.apiservice.shared.utils.TokenExtractionUtils;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private static final int EXPIRATION_TIME = 1000 * 60 * 15;
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final BlackListRepositoryJpa blackListRepository;

    public String generateJwtToken(UUID id,
                                   String username,
                                   UserRole role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role",
                       role
                )
                .claim("userID",
                       id
                )
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role")
                .toString();
    }

    public UUID getIdFromJwtToken(String token) {
        return UUID.fromString(Jwts.parserBuilder()
                                       .setSigningKey(SECRET_KEY)
                                       .build()
                                       .parseClaimsJws(token)
                                       .getBody()
                                       .get("userID")
                                       .toString());
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            return !blackListRepository.existsByToken(token);
        } catch (JwtException e) {
            return false;
        }
    }

    public void invalidateToken(HttpServletRequest request) {
        final String token = TokenExtractionUtils.extractToken(request);

        final BlackListEntity toInvalidate = BlackListEntity.builder()
                .token(token)
                .blacklistedAt(OffsetDateTime.now())
                .build();

        blackListRepository.save(toInvalidate);
    }
}

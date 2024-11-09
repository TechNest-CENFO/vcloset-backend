package com.example.vcloset.logic.entity.auth;

import com.example.vcloset.logic.entity.auth.passwordResetEntity.PasswordResetEntity;
import com.example.vcloset.logic.entity.auth.passwordResetEntity.PasswordResetEntityRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("300000")
    private long passwordResetJwtExpiration;

    @Autowired
    private PasswordResetEntityRepository passwordResetEntityRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generatePasswordResetToken(String email, String token) {
        return buildPasswordResetToken(email, passwordResetJwtExpiration, token);
    }


    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String buildPasswordResetToken(
            String email,
            long passwordResetExpiration,
            String token
    ) {
        PasswordResetEntity passwordResetEntity = new PasswordResetEntity();
        passwordResetEntity.setToken(token);
        passwordResetEntity.setEmail(email);
        passwordResetEntity.setExpirationDate(new Date(System.currentTimeMillis() + passwordResetJwtExpiration));
        passwordResetEntityRepository.save(passwordResetEntity);
        return Jwts.builder()
                .setSubject(email)
                .claim("token", token)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + passwordResetExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
/*
    public boolean validatePasswordResetToken(String token, String email) {
        try {
            // Extraer los claims del token
            Claims claims = extractAllClaims(token);

            // Validar que el token no haya expirado
            if (claims.getExpiration().before(new Date())) {
                return false; // Token expirado
            }

            // Verificar que el email en el token coincida con el email del usuario
            String tokenEmail = claims.getSubject();
            if (!tokenEmail.equals(email)) {
                return false; // El email no coincide
            }

            // Extraer y validar el UUID del claim "token"
            String storedUuid = claims.get("token", String.class);
            if (storedUuid == null || storedUuid.isEmpty()) {
                return false; // UUID inválido
            }

            return true; // El token es válido
        } catch (Exception e) {
            // Si hay algún error en la validación, el token es inválido
            return false;
        }
    }*/
}

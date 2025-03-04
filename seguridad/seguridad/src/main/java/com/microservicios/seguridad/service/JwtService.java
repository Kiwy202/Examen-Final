package com.microservicios.seguridad.service;

import com.microservicios.seguridad.model.Usuario;
import com.microservicios.seguridad.repository.UsuarioRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final UsuarioRepository usuarioRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (userDetails == null) {
            System.out.println("ERROR: userDetails es NULL, intentando recuperar usuario...");
            String username = extractUsername(token);
            System.out.println("Buscando usuario en la base de datos: " + username);

            userDetails = usuarioRepository.findByUsername(username)
                    .orElse(null);

            if (userDetails == null) {
                System.out.println("ERROR: No se encontró usuario en la base de datos.");
                return false;
            }
        }

        final String usernameFromToken = extractUsername(token);
        boolean isValid = usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token);

        System.out.println("¿El token es válido?: " + isValid);
        return isValid;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

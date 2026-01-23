package com.luciano.pruebatecnicasupermercado.security;

import com.luciano.pruebatecnicasupermercado.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshExpiration;

    public String generateToken(final Usuario usuario) {
        return buildToken(usuario, jwtExpiration);
    }

    public String generateRefreshToken(final Usuario usuario) {
        return buildToken(usuario, refreshExpiration);
    }

    public String buildToken(final Usuario usuario, final long expiration) {
        return Jwts.builder()
                .claims(Map.of("name", usuario.getNombre())) //Información adicional para el token
                .subject(usuario.getUsername()) //Identificación del usuario en el token
                .issuedAt(new Date(System.currentTimeMillis())) //Cuando se creó la clave
                .expiration(new Date(System.currentTimeMillis() + expiration)) //Cuando va a expirar
                .signWith(getSignInKey())
                .compact();
    }

    // Validar y leer token

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Valida que el usuario del token sea el mismo que el de la BD y que no haya expirado.
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

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Aquí se usa la clave para verificar la firma
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //Decodificamos nuestra clave de las variables de entorno
        return Keys.hmacShaKeyFor(keyBytes); //Genera llave secreta
    }

}

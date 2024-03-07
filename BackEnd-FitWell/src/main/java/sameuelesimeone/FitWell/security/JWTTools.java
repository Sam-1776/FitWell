package sameuelesimeone.FitWell.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
import sameuelesimeone.FitWell.models.User;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(User user){
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24))
                .subject(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public void verifyToken(String token){
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).build().parse(token);
        }catch (Exception ex){
            throw new UnauthorizedExeption("Problemi con il token. Effettua il login!");
        }
    }

    public String takeIdByToken(String token){
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).build().parseSignedClaims(token).getPayload().getSubject();
    }
}

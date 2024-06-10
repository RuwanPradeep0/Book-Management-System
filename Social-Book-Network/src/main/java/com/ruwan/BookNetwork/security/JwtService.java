package com.ruwan.BookNetwork.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {


    private final long jwtExpiration = 8640000;

//    @Value("@{spring.jpa.application.security.jwt.secret-key}")
    private String secretKey ="404E635266556A586E3272357538782F413F4428472B4B6250646367566B5970";


    public String extractUserName(String token) {
        return extractClaim(token , Claims::getSubject);

    }

    public <T> T extractClaim(String token , Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return  claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                ;
    }


    public  String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    private  String generateToken(Map<String,Object> claims, UserDetails userDetails) {
        return buildToken(claims , userDetails , Long.parseLong(String.valueOf(jwtExpiration)));
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long jwtExpiration) {

        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration) )
                .claim("authorities" , authorities)
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

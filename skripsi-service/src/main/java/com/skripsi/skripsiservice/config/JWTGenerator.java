package com.skripsi.skripsiservice.config;



import com.skripsi.skripsiservice.domain.UserDomain;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JWTGenerator {
    public String generate(UserDomain userDomain) {

        Claims claims= Jwts.claims()
                .setSubject(userDomain.getEmail());
        claims.put("userId",userDomain.getId());
        claims.put("email",userDomain.getEmail());
        claims.put("userName",userDomain.getUsername());


        return Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS512,"IATI")
                .claim("userId",userDomain.getId())
                .compact();

    }
}

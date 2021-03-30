package com.skripsi.skripsiservice.config;


import com.skripsi.skripsiservice.domain.UserDomain;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JWTValidator {
    private String secret="IATI";


    public UserDomain validate(String token) {

        UserDomain userDomain = null;
        try {

        Claims body= Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();

        userDomain = new UserDomain();

        userDomain.setUsername(body.getSubject());
        userDomain.setEmail((String)body.get("email"));
        userDomain.setPassword((String)body.get("userpassword"));
        }
        catch (Exception e){
            System.out.println(e);
        }

        return userDomain;
    }
}

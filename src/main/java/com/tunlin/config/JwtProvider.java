package com.tunlin.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtProvider {

     static SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());

    public static String generateToken(Authentication auth){

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email",auth.getName())
                .signWith(key)
                .compact();
    }

    public static String getEmailFromJwtToken(String jwt){

        jwt = jwt.substring(7);

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;

    }

//    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
//
//        Set<String> auths = new HashSet<>();
//
//        for (GrantedAuthority authority: authorities){
//            auths.add(authority.getAuthority());
//        }
//        return String.join("",auths);
//
//    }

}
